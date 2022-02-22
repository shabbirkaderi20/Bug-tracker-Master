package com.comments.service;

import com.comments.dto.CommentsDto;
import com.comments.entity.Comments;
import com.comments.repository.CommentsRepository;
import com.comments.vo.Bug;
import com.comments.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class CommentsService {

    @Autowired
    CommentsRepository commentsRepository;

    public Comments addComment(CommentsDto commentsDto) {

        User user= new RestTemplate().getForObject("http://localhost:9002/users/getUser/"+
                commentsDto.getPostId(), User.class);

        Bug bug= new RestTemplate().getForObject("http://localhost:9004/bugs/bugById/"+
                commentsDto.getBugId(), Bug.class);

        Comments comments= new Comments();
            comments.setComment(commentsDto.getComment());
            comments.setPostId(commentsDto.getPostId());
            comments.setBugId(commentsDto.getBugId());

        if(comments.getComment().contains("#")) {
            comments.setProblemHighlighted(true);
        }

        if(comments.getComment().contains("@")) {
            comments.setUserMentioned(true);
        }

        return commentsRepository.saveAndFlush(comments);
    }

    public Boolean deleteComment(Long commentId) {

        Comments comments= commentsRepository.findByCommentId(commentId);

        if(comments!= null) {
            commentsRepository.deleteByCommentId(commentId);
            return true;

        }else {
            return false;
        }
    }

    public List<Comments> getCommentsByBugId(Long bugId) {

        Bug bug= new RestTemplate().getForObject("http://localhost:9004/bugs/bugById/"
                + bugId, Bug.class);

        List<Comments> commentsList= commentsRepository.findByBugId(bugId);

        return commentsList;
    }
}
