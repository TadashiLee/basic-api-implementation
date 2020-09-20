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

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @PostMapping("/rsEvent/{id}/vote")
    public ResponseEntity voteArsEvent(@RequestBody Vote vote, @PathVariable int id) {

        Optional<RsEventEntity> rsEventEntity = rsEventRepository.findById(id);
        Optional<UserEntity> userEntity = userRepository.findById(vote.getUserId());
        if (!rsEventEntity.isPresent() || !userEntity.isPresent() || vote.getVoteNum() > userEntity.get().getVoteNum()) {
            return ResponseEntity.badRequest().build();
        }

        VoteEntity voteEntity = VoteEntity.builder()
                .num(vote.getVoteNum())
                .time(vote.getTime())
                .rsEvents(rsEventEntity.get())
                .user(userEntity.get())
                .build();
        voteRepository.save(voteEntity);

        UserEntity user = userEntity.get();
        user.setVoteNum(user.getVoteNum() - vote.getVoteNum());
        userRepository.save(user);

        RsEventEntity rsEvent = rsEventEntity.get();
        rsEvent.setVoteNum(rsEvent.getVoteNum() + vote.getVoteNum());
        rsEventRepository.save(rsEvent);

        return ResponseEntity.created(null).build();
    }

//    @GetMapping("/vote")
//
//    public ResponseEntity<List<Vote>> getVotes(@RequestParam int userId, @RequestParam int rsEventId){
//        List<VoteEntity> votes = voteRepository.findALLByUserIdAndRsEventId(userId, rsEventId);
//        return ResponseEntity.ok(votes.stream().map(vote -> Vote.builder()
//                .userId(vote.getUser().getId())
//                .voteNum(vote.getNum())
//                .time(vote.getTime())
//                .rsEventId(vote.getRsEvents().getId())
//                .build()).collect(Collectors.toList()));
//    }

    @GetMapping("/votes")
    public ResponseEntity<List<Vote>> getVotesByTime(@RequestParam Timestamp start, @RequestParam Timestamp end){
        List<VoteEntity> votes = voteRepository.findAllByTimeBetween(start, end);
        return ResponseEntity.ok(votes.stream().map(vote -> Vote.builder()
                .userId(vote.getUser().getId())
                .voteNum(vote.getNum())
                .time(vote.getTime())
                .rsEventId(vote.getRsEvents().getId())
                .build()).collect(Collectors.toList()));
    }

}
