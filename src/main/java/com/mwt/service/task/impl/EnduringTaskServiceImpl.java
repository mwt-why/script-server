package com.mwt.service.task.impl;

import com.mongodb.client.result.UpdateResult;
import com.mwt.beans.comm.SimpleMap;
import com.mwt.beans.res.Account;
import com.mwt.beans.res.Role;
import com.mwt.beans.task.EnduringTask;
import com.mwt.beans.task.TaskAccount;
import com.mwt.repository.task.EnduringTaskRepository;
import com.mwt.service.task.EnduringTaskService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class EnduringTaskServiceImpl implements EnduringTaskService {

    private static final String ENDURING_TASK_COLLECTION = "enduringTask";

    @Resource
    private MongoTemplate mongoTemplate;

    @Resource
    private EnduringTaskRepository enduringTaskRepository;

    /**
     * 添加好友的任务队列
     */
    private Queue<String> addFriendsTaskQueue = new LinkedBlockingQueue<>();

    /**
     * 组队map,key是要加好友的任务id
     */
    private Map<String, List<String>> team = new ConcurrentHashMap<>();

    /**
     * 同意好友之后的反馈，为了删除team里面的数据
     */
    private Map<String, Set<String>> reportCache = new ConcurrentHashMap<>();


    private int getNeedAccountCount() {
        return 3;
    }

    @Transactional
    public synchronized EnduringTask createTask(String deviceId) {
        List<TaskAccount> taskAccounts = listAvailableAccount(getNeedAccountCount());
        if (Objects.isNull(taskAccounts) || taskAccounts.isEmpty()) {
            return null;
        }
        EnduringTask immature = EnduringTask.builder().
                status(0).
                deviceId(deviceId).
                accountBeans(taskAccounts).
                build();
        immature.setLastUpdateTime(LocalDateTime.now());
        mongoTemplate.save(immature);
        return immature;
    }

    @Override
    public EnduringTask getTask(String deviceId) {
        EnduringTask task = mongoTemplate.findOne(
                Query.query(Criteria.where("deviceId").is(deviceId)), EnduringTask.class);
        if (Objects.isNull(task)) {
            task = createTask(deviceId);
        }
        return task;
    }

    @Override
    public EnduringTask getEnduringTaskById(String id) {
        return enduringTaskRepository.findById(id).get();
    }

    private List<TaskAccount> listAvailableAccount(int count) {
        List<TaskAccount> taskAccounts = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            Account account = mongoTemplate.findAndModify(
                    Query.query(Criteria.where("status").is(0)),
                    new Update().set("status", 1),
                    FindAndModifyOptions.options().returnNew(false),
                    Account.class);
            if (Objects.isNull(account))
                break;
            TaskAccount taskAccount = new TaskAccount();
            BeanUtils.copyProperties(account, taskAccount);
            taskAccounts.add(taskAccount);
        }
        return taskAccounts;
    }

    @Override
    public int deleteTask(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("_id").is(id)));
        return 1;
    }

    @Override
    public long updateTask(String id, SimpleMap simpleMap) {
        Query query = new Query(Criteria.where("_id").is(id));
        Update update = new Update().set(simpleMap.getKey(), simpleMap.getValue());
        UpdateResult result = mongoTemplate.updateFirst(query, update, ENDURING_TASK_COLLECTION);
        return result.getModifiedCount();
    }

    public long updateAccount(String id, int accountIndex, String key, Object value) {
        return updateAccount(id, accountIndex, key, value, false);
    }

    public long updateAccount(String id, int accountIndex, String key, Object value, boolean unset) {
        Query query = new Query(Criteria.where("_id").is(id));
        String filter = "accountBeans." + accountIndex + "." + key;
        Update update = new Update().set(filter, value);
        if (unset) {
            update.unset(key);
        }
        UpdateResult result = mongoTemplate.updateFirst(query, update, ENDURING_TASK_COLLECTION);
        return result.getModifiedCount();
    }

    @Override
    public long updateCurAccount(String id, SimpleMap simpleMap) {
        int curAccountIndex = getCurAccountIndex(id);
        return updateAccount(id, curAccountIndex, simpleMap.getKey(), simpleMap.getValue());
    }

    public long updateRole(String id, int accountIndex, int roleIndex, String key, Object value) {
        Query query = new Query(Criteria.where("_id").is(id));
        String filter = "accountBeans." + accountIndex + ".roles." + roleIndex + "." + key;
        Update update = new Update().set(filter, value);
        UpdateResult result = mongoTemplate.updateFirst(query, update, ENDURING_TASK_COLLECTION);
        return result.getModifiedCount();
    }

    @Override
    public long updateCurRole(String id, SimpleMap simpleMap) {
        int curAccountIndex = getCurAccountIndex(id);
        int curRoleIndex = getCurRoleIndex(id);
        return updateRole(id, curAccountIndex, curRoleIndex, simpleMap.getKey(), simpleMap.getValue());
    }

    @Override
    public long resetAccountRole(String id) {
        int accountCount = getAccountCount(id);
        //重置账号状态
        for (int i = 0; i < accountCount; i++) {
            updateAccount(id, i, "status", 0);
            int roleCount = getRoleCount(id, i);
            if (roleCount == 0) {
                continue;
            }
            //重置角色状态
            for (int j = 0; j < roleCount; j++) {
                updateRole(id, i, j, "status", 0);
            }
        }
        return accountCount;
    }

    private long tagCurAccountIsDo(String id) {
        SimpleMap simpleMap = new SimpleMap();
        simpleMap.put("status", 1);
        updateCurRole(id, simpleMap);
        return updateCurAccount(id, simpleMap);
    }


    @Override
    public long begin(String id) {
        EnduringTask taskBean = enduringTaskRepository.findById(id).get();
        int status = taskBean.getStatus();
        if (-1 != status) {
            return 9;   //当前任务还没不能执行脚本任务
        }
        resetAccountRole(id);
        tagCurAccountIsDo(id);
        SimpleMap simpleMap = new SimpleMap();
        simpleMap.put("status", 1);
        return updateTask(id, simpleMap);
    }

    @Override
    public long finish(String id) {
        resetAccountRole(id);
        SimpleMap simpleMap = new SimpleMap();
        simpleMap.put("status", -1);
        return updateTask(id, simpleMap);
    }

    private int getCurAccountIndex(String id) {
        return getCurAccountOrRoleIndex(id, true);
    }

    private int getCurRoleIndex(String id) {
        return getCurAccountOrRoleIndex(id, false);
    }


    private int getRoleCount(String id, int accountIndex) {
        EnduringTask taskBean = enduringTaskRepository.findById(id).get();
        List<TaskAccount> accountBeans = taskBean.getAccountBeans();
        if (Objects.isNull(accountBeans)) {
            return 0;
        }
        TaskAccount accountBean = accountBeans.get(accountIndex);
        List<Role> roles = accountBean.getRoles();
        if (Objects.isNull(roles)) {
            return 0;
        }
        return roles.size();
    }

    private int getAccountCount(String id) {
        EnduringTask taskBean = enduringTaskRepository.findById(id).get();
        List<TaskAccount> accountBeans = taskBean.getAccountBeans();
        if (Objects.nonNull(accountBeans)) {
            return accountBeans.size();
        }
        return 0;
    }

    /**
     * 获取当前账号或角色下标
     *
     * @param id
     * @return
     */
    private int getCurAccountOrRoleIndex(String id, boolean isAccount) {
        EnduringTask task = enduringTaskRepository.findById(id).get();
        String curAccount = task.getCurAccount();
        if (Objects.isNull(curAccount)) {
            return 0;
        }
        List<TaskAccount> accountBeans = task.getAccountBeans();
        int index = 0;
        for (TaskAccount a : accountBeans) {
            if (curAccount.equals(a.getAccount())) {
                if (isAccount) {
                    return index;
                } else {
                    return a.getCurRoleIndex();
                }
            }
            index++;
        }
        throw new RuntimeException("当前账号不在该任务中");
    }

    /**
     * 添加好友
     *
     * @return
     */
    @Override
    public synchronized List<String> addFriends(String id) {
        List<String> leaderOrMember = getTeamMember(id);
        if (!leaderOrMember.isEmpty())
            return leaderOrMember;
        //判断该任务是否已经添加到待组队任务队列中了
        if (addFriendsTaskQueue.contains(id)) {
            return Collections.EMPTY_LIST;
        }
        addFriendsTaskQueue.add(id);
        //如果任务队列满足了5个，那就可以开始加好友了
        if (addFriendsTaskQueue.size() == 3) {
            String leader = moveToTeam();
            if (leader.equals(id))
                return team.get(id);
            else
                return Arrays.asList(leader);
        }
        return Collections.EMPTY_LIST;
    }

    private List<String> getTeamMember(String id) {
        if (team.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        Iterator<Map.Entry<String, List<String>>> iterator = team.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, List<String>> next = iterator.next();
            String leader = next.getKey();
            List<String> member = next.getValue();
            //任务是添加好友的组长,返回队员
            if (leader.equals(id)) {
                return member;
            }
            //任务是队员返回队长
            for (String v : member) {
                if (v.equals(id)) {
                    return Arrays.asList(leader);
                }
            }
        }
        return Collections.EMPTY_LIST;
    }

    /**
     * 把满足添加好友的任务添加到team中
     *
     * @return 队长的id
     */
    private String moveToTeam() {
        Iterator<String> iterator = addFriendsTaskQueue.iterator();
        String leader = iterator.next();
        List<String> tmpTeam = new ArrayList<>();
        while (iterator.hasNext()) {
            tmpTeam.add(iterator.next());
        }
        team.put(leader, tmpTeam);
        reportCache.put(leader, new HashSet<>());
        return leader;
    }

    /**
     * 检测队长是否已经添加完毕好友
     *
     * @param leaderId
     * @return
     */
    @Override
    public boolean checkLeaderAddFriendOver(String leaderId) {
        EnduringTask enduringTask = enduringTaskRepository.findById(leaderId).get();
        List<String> friends = enduringTask.getFriends();
        if (Objects.isNull(friends) || friends.isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * 这里的添加有可能是添加好友，也有可能是好友同意的请求
     */
    @Override
    public long reportAddFriend(String id) {
        Iterator<Map.Entry<String, List<String>>> iterator = team.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, List<String>> next = iterator.next();
            String key = next.getKey();
            List<String> friends = next.getValue();
            SimpleMap simpleMap = new SimpleMap();
            //队长反馈的好友添加完毕，需要把所有好友添加到好友列表中
            if (key.equals(id)) {
                simpleMap.put("friends", friends);
                return updateTask(id, simpleMap);
            } else {
                simpleMap.put("friends", Arrays.asList(key));
                updateTask(id, simpleMap);
            }
            //记录反馈，所有添加或同意完毕就干掉添加好友的相关缓存
            for (String f : friends) {
                if (f.equals(id)) {
                    Set<String> members = reportCache.get(key);
                    members.add(id);
                    reportCache.put(key, members);
                    checkForClearCache(key);
                    return 1;
                }
            }
        }
        return 0;
    }

    private void checkForClearCache(String leaderId) {
        List<String> t = team.get(leaderId);
        Set<String> members = reportCache.get(leaderId);
        if (members.size() >= t.size()) {    //说明添加好友完毕
            reportCache.remove(leaderId);
            team.remove(leaderId);
        }
    }

    private Queue<String> teamQueue = new LinkedBlockingQueue<>();

    private Map<String, List<String>> leadersCache = new ConcurrentHashMap<>();

    /**
     * 判断队员已经就绪
     *
     * @return
     */
    private int memberIsPrepare(String id) {
        List<String> members = leadersCache.get(id);
        if (teamQueue.containsAll(members)) {
            return members.size();
        }
        return 0;
    }

    @Override
    public long teamUp(String id) {
        //判断是否是队长
        if (leadersCache.containsKey(id)) {
            //判断队员是否已经准备就绪
            int memberSize = memberIsPrepare(id);
            return memberSize;
        }
        EnduringTask enduringTask = enduringTaskRepository.findById(id).get();
        List<String> friends = enduringTask.getFriends();
        if (friends.size() > 1) {   //说明是队长
            if (teamQueue.containsAll(friends)) {
                leadersCache.put(id, friends);
                return friends.size();
            } else {
                leadersCache.put(id, friends);
                return 0;
            }
        }
        if (!teamQueue.contains(id)) {
            teamQueue.add(id);
        }
        return 9;   //表示是一个队员
    }

    /**
     * 解散组好的队伍
     *
     * @param leaderId
     */
    @Override
    public void disband(String leaderId) {
        List<String> members = leadersCache.get(leaderId);
        if (Objects.nonNull(members) && !members.isEmpty()) {
            teamQueue.removeAll(members);
            leadersCache.remove(leaderId);
        }
    }

    @Override
    public void add(EnduringTask bean) {

    }

    @Override
    public void update(EnduringTask bean) {

    }

    @Override
    public void delete(String id) {

    }

    @Override
    public Page<EnduringTask> list(Pageable pageable) {
        return enduringTaskRepository.findAll(pageable);
    }

    /**
     * 这个接口主要是把任务重置到刚创建的那个状态
     *
     * @param id
     */
    @Override
    public void reset(String id) {
        EnduringTask enduringTask = getEnduringTaskById(id);
        List<TaskAccount> accountBeans = enduringTask.getAccountBeans();
        if (Objects.nonNull(accountBeans) && !accountBeans.isEmpty()) {
            for (int i = 0; i < accountBeans.size(); i++) {
                updateAccount(id, i, "roles", "", true);
            }
        }
        SimpleMap<Integer> simpleMap = new SimpleMap<>();
        simpleMap.put("status", 0);
        updateTask(id, simpleMap);
    }
}
