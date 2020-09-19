package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.Vote;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class VoteController {
    private final UserRepository userRepository;
    private final RsEventRepository rsEventRepository;
    private final VoteRepository voteRepository;

    public VoteController(UserRepository userRepository, RsEventRepository rsEventRepository, VoteRepository voteRepository) {
        this.userRepository = userRepository;
        this.rsEventRepository = rsEventRepository;
        this.voteRepository = voteRepository;
    }

    @PostMapping("/vote/event/{id}")
    public ResponseEntity voteArsEvent(@RequestBody Vote vote, @PathVariable int id){
        if (!userRepository.existsById(vote.getUserId())) {
            return ResponseEntity.badRequest().build();
        }
        VoteEntity voteEntity = VoteEntity.builder()
                .num(vote.getVoteNum())
                .time(vote.getTime())
                .rsEvents(RsEventEntity.builder()
                        .id(id)
                        .build())
                .user(UserEntity.builder()
                        .id(vote.getUserId())
                        .build())
                .build();
        voteRepository.save(voteEntity);
        return ResponseEntity.created(null).build();
    }
}
