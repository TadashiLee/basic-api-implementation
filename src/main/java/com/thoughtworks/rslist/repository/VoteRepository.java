package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.entity.VoteEntity;
import org.springframework.data.repository.CrudRepository;

import java.sql.Timestamp;
import java.util.List;

public interface VoteRepository extends CrudRepository<VoteEntity, Integer> {
    List<VoteEntity> findAll();
//    List<VoteEntity> findALLByUserIdAndRsEventId(int userId, int rsEventId);
    List<VoteEntity> findAllByTimeBetween(Timestamp start, Timestamp end);
}
