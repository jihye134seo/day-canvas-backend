package com.daycanvas.domain.post;

import com.daycanvas.dto.post.DayImageMappingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    @Value("${flask.api.url}")
    private String flaskApiUrl;
    private final RestTemplate restTemplate = new RestTemplate();

    private final PostRepository repository;

    public Long save(Post post) {
        ResponseEntity<String> response = restTemplate.postForEntity(flaskApiUrl, post.getContent().getBytes(), String.class);
        post.setImagePath(response.getBody());
        return repository.save(post).getId();
    }

    public Post findById(Long postId) {
        try {
            Optional<Post> OptionalPost = repository.findById(postId);

            if (OptionalPost.isPresent()) {
                return OptionalPost.get();
            }
            else {
                throw new EmptyResultDataAccessException("Post not found with id: " + postId, 1);
            }
        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException("Post not found with id: " + postId);
        }
    }

    public List<DayImageMappingDto> findAllByMonth(int year, int month) { // @Todo 유저 필터링(id, 세선) 추가
        return repository.findAllByMonthAndYear(year, month);
    }

    public Long update(Post post) {
        try {
            Optional <Post> OptionalPost = repository.findById(post.getId());

            if (OptionalPost.isPresent()) {
                Post savedPost = OptionalPost.get();
                savedPost.setContent(post.getContent());
                ResponseEntity<String> response = restTemplate.postForEntity(flaskApiUrl, post.getContent().getBytes(), String.class);
                savedPost.setImagePath(response.getBody());
                repository.save(savedPost);
                return savedPost.getId();
            }
        } catch (EmptyResultDataAccessException ex) {
            throw new RuntimeException("Post not found for deletion.");
        } catch (DataAccessException ex) {
            // 그 외 데이터베이스 관련 예외 처리
            throw new RuntimeException("Error finding exist post", ex);
        }
        return null;
    }

    public void delete(Long postId) {
        try {
            repository.deleteById(postId);
        } catch (EmptyResultDataAccessException ex) {
            // 삭제할 대상이 없는 경우에 대한 예외 처리
            throw new RuntimeException("Post with ID" + postId + " not found for deletion.");
        } catch (DataAccessException ex) {
            // 그 외 데이터베이스 관련 예외 처리
            throw new RuntimeException("Error deleting user with ID " + postId, ex);
        }
    }

}
