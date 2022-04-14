package com.mwt.service.task.impl;

import com.mongodb.client.result.UpdateResult;
import com.mwt.beans.comm.SimpleMap;
import com.mwt.beans.res.Account;
import com.mwt.beans.res.Role;
import com.mwt.beans.task.EnduringTask;
import com.mwt.beans.task.TaskAccount;
import com.mwt.repository.task.EnduringTaskRepository;
import com.mwt.service.common.TempService;
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

    private static final String COLLECTION_NAME = "enduringTask";

    @Resource
    private MongoTemplate mongoTemplate;

    @Resource
    private EnduringTaskRepository enduringTaskRepository;

    @Resource
    private TempService tempService;

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
        return updateTask(id, simpleMap.getKey(), simpleMap.getValue());
    }

    public long updateTask(String id, String key, Object value) {
        Query query = new Query(Criteria.where("_id").is(id));
        Update update = new Update().set(key, value);
        UpdateResult result = mongoTemplate.updateFirst(query, update, COLLECTION_NAME);
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
        UpdateResult result = mongoTemplate.updateFirst(query, update, COLLECTION_NAME);
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
        UpdateResult result = mongoTemplate.updateFirst(query, update, COLLECTION_NAME);
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

    private int getFriendCount() {
        return 3;
    }

    /**
     * 添加好友
     *
     * @return
     */
    @Override
    @Transactional
    public synchronized List<String> addFriend(String id) {
        EnduringTask task = getEnduringTaskById(id);
        List<String> existFriends = task.getFriends();
        //说明还没有分配可添加的好友
        if (Objects.isNull(existFriends) || existFriends.isEmpty()) {
            List<EnduringTask> friends = findFriends(getFriendCount());
            if (Objects.isNull(friends)) {
                return null;
            }
            //队友拥有队长好友
            List<String> friendIds = new ArrayList<>(friends.size());
            for (EnduringTask et : friends) {
                String fd = et.getId();
                if (fd.equals(id))
                    continue;
                updateTask(fd, "friends", Arrays.asList(id));
                updateTask(fd, "isLeader", false);
                friendIds.add(fd);
            }
            //修改队长拥有的好友
            updateTask(id, "friends", friendIds);
            updateTask(id, "isLeader", true);
            int result = tempService.save(id, false);
            if (-1 == result) {
                throw new RuntimeException("无法往temp中插入自定的key");
            }
            //给队长返回好友准备添加
            return friendIds;
        }
        return existFriends;
    }

    public List<EnduringTask> findFriends(int friendCount) {
        Query query = Query.query(Criteria.where("friends").exists(false));
        List<EnduringTask> friends = mongoTemplate.find(query, EnduringTask.class);
        if (Objects.isNull(friends) || friends.size() < friendCount) {
            return null;
        }
        return friends.subList(0, friendCount);
    }

    private Map<String, Set<String>> forClearMap = new ConcurrentHashMap<>();

    /**
     * 添加好友完毕反馈
     *
     * @param leaderId
     * @param selfId
     */
    @Override
    public void reportAddFriend(String leaderId, String selfId) {
        //队长
        if (leaderId.equals(selfId)) {
            tempService.updateByKey(leaderId, true);
        } else {
            Set<String> members = forClearMap.get(leaderId);
            if (Objects.isNull(members)) {
                members = new HashSet<>();
            }
            members.add(selfId);
            checkForClear(leaderId);
        }
    }

    private void checkForClear(String leaderId) {
        EnduringTask enduringTask = getEnduringTaskById(leaderId);
        List<String> friends = enduringTask.getFriends();
        if (Objects.isNull(friends) || friends.isEmpty()) {
            throw new IllegalArgumentException("该任务还没添加好友，不存在好友反馈");
        }
        Set<String> members = forClearMap.get(leaderId);
        //开始清理临时表，和forClearMap
        if (friends.contains(members)) {
            //清除队长的是否添加好友完毕状态
            tempService.delete(leaderId);
            //清除上面的记录缓存
            forClearMap.remove(leaderId);
        }
    }

    /**
     * 检查队长添加好友是否完毕
     *
     * @param leaderId
     * @return
     */
    @Override
    public boolean checkLeaderAddFriendOver(String leaderId) {
        return (boolean) tempService.getByKey(leaderId);
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
