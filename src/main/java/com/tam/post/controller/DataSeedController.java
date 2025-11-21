package com.tam.post.controller;

import com.tam.post.dto.ApiResponse;
import com.tam.post.entity.Comment;
import com.tam.post.entity.Like;
import com.tam.post.entity.Post;
import com.tam.post.entity.Share;
import com.tam.post.repository.CommentRepository;
import com.tam.post.repository.LikeRepository;
import com.tam.post.repository.PostRepository;
import com.tam.post.repository.ShareRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/seed")
@RequiredArgsConstructor
public class DataSeedController {

    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final ShareRepository shareRepository;

    @PostMapping
    public ApiResponse<String> seedData() {
        // Xóa dữ liệu cũ
        commentRepository.deleteAll();
        likeRepository.deleteAll();
        shareRepository.deleteAll();
        postRepository.deleteAll();

        // User IDs mẫu
        String user1 = "304af409-1bae-44a8-a777-0c41327854c2"; // User 1
        String user2 = "405bf509-2cbf-55b9-b888-1d52438965d3"; // User 2
        String user3 = "506cg609-3dcg-66c0-c999-2e63549076e4"; // User 3

        // Base time để tạo timestamps
        Instant baseTime = Instant.now().minus(7, ChronoUnit.DAYS);

        // ========== TẠO POSTS ==========
        List<Post> posts = new ArrayList<>();

        // Post 1 - từ user1 (7 ngày trước)
        Post post1 = Post.builder()
                .userId(user1)
                .content("Chào mọi người! Đây là bài post đầu tiên của tôi trên mạng xã hội này. 🎉")
                .mediaUrl("https://images.unsplash.com/photo-1516117172878-fd2c41f4a759")
                .likeCount(2)
                .commentCount(3)
                .shareCount(0)
                .createdBy(user1)
                .lastUpdatedBy(user1)
                .publicity(2) // public
                .build();
        post1.setCreatedDate(baseTime);
        post1.setModifiedDate(baseTime);
        post1 = postRepository.save(post1);
        posts.add(post1);

        // Post 2 - từ user2 (6 ngày trước)
        Post post2 = Post.builder()
                .userId(user2)
                .content("Hôm nay thời tiết thật đẹp! ☀️ Mọi người có kế hoạch gì thú vị không?")
                .mediaUrl("https://images.unsplash.com/photo-1501594907352-04cda38ebc29")
                .likeCount(2)
                .commentCount(2)
                .shareCount(0)
                .createdBy(user2)
                .lastUpdatedBy(user2)
                .publicity(2)
                .build();
        post2.setCreatedDate(baseTime.plus(1, ChronoUnit.DAYS));
        post2.setModifiedDate(baseTime.plus(1, ChronoUnit.DAYS));
        post2 = postRepository.save(post2);
        posts.add(post2);

        // Post 3 - từ user3 (5 ngày trước)
        Post post3 = Post.builder()
                .userId(user3)
                .content("Check out my new video about web development! 💻 #coding #webdev")
                .mediaUrl("https://images.unsplash.com/photo-1498050108023-c5249f4df085")
                .likeCount(2)
                .commentCount(2)
                .shareCount(1)
                .createdBy(user3)
                .lastUpdatedBy(user3)
                .publicity(2)
                .build();
        post3.setCreatedDate(baseTime.plus(2, ChronoUnit.DAYS));
        post3.setModifiedDate(baseTime.plus(2, ChronoUnit.DAYS));
        post3 = postRepository.save(post3);
        posts.add(post3);

        // Post 4 - từ user1 (4 ngày trước)
        Post post4 = Post.builder()
                .userId(user1)
                .content("Chia sẻ một số suy nghĩ về công việc hôm nay... Đôi khi ta cần dừng lại và nhìn lại những gì đã làm được! 💪")
                .mediaUrl("https://images.unsplash.com/photo-1522202176988-66273c2fd55f")
                .likeCount(1)
                .commentCount(1)
                .shareCount(0)
                .createdBy(user1)
                .lastUpdatedBy(user1)
                .publicity(2)
                .build();
        post4.setCreatedDate(baseTime.plus(3, ChronoUnit.DAYS));
        post4.setModifiedDate(baseTime.plus(3, ChronoUnit.DAYS));
        post4 = postRepository.save(post4);
        posts.add(post4);

        // Post 5 - từ user2 (3 ngày trước)
        Post post5 = Post.builder()
                .userId(user2)
                .content("Happy weekend everyone! 🎊 Time to relax and enjoy!")
                .mediaUrl(null)
                .likeCount(1)
                .commentCount(0)
                .shareCount(0)
                .createdBy(user2)
                .lastUpdatedBy(user2)
                .publicity(2)
                .build();
        post5.setCreatedDate(baseTime.plus(4, ChronoUnit.DAYS));
        post5.setModifiedDate(baseTime.plus(4, ChronoUnit.DAYS));
        post5 = postRepository.save(post5);
        posts.add(post5);

        // Post 6 - từ user3 (2 ngày trước)
        Post post6 = Post.builder()
                .userId(user3)
                .content("Học lập trình di động thật thú vị! Flutter hay React Native nhỉ? 🤔 #mobile #development")
                .mediaUrl("https://images.unsplash.com/photo-1512941937669-90a1b58e7e9c")
                .likeCount(0)
                .commentCount(3)
                .shareCount(1)
                .createdBy(user3)
                .lastUpdatedBy(user3)
                .publicity(2)
                .build();
        post6.setCreatedDate(baseTime.plus(5, ChronoUnit.DAYS));
        post6.setModifiedDate(baseTime.plus(5, ChronoUnit.DAYS));
        post6 = postRepository.save(post6);
        posts.add(post6);

        // Post 7 - từ user1 (1 ngày trước)
        Post post7 = Post.builder()
                .userId(user1)
                .content("Weekend vibes! 🌊🏖️ Beach day with friends!")
                .mediaUrl("https://images.unsplash.com/photo-1507525428034-b723cf961d3e")
                .likeCount(0)
                .commentCount(0)
                .shareCount(0)
                .createdBy(user1)
                .lastUpdatedBy(user1)
                .publicity(2)
                .build();
        post7.setCreatedDate(baseTime.plus(6, ChronoUnit.DAYS));
        post7.setModifiedDate(baseTime.plus(6, ChronoUnit.DAYS));
        post7 = postRepository.save(post7);
        posts.add(post7);

        // Post 8 - từ user2 (12 giờ trước)
        Post post8 = Post.builder()
                .userId(user2)
                .content("Vừa hoàn thành dự án lớn! 🎯 Cảm ơn team đã support!")
                .mediaUrl(null)
                .likeCount(0)
                .commentCount(0)
                .shareCount(1)
                .createdBy(user2)
                .lastUpdatedBy(user2)
                .publicity(2)
                .build();
        post8.setCreatedDate(Instant.now().minus(12, ChronoUnit.HOURS));
        post8.setModifiedDate(Instant.now().minus(12, ChronoUnit.HOURS));
        post8 = postRepository.save(post8);
        posts.add(post8);

        // Post 9 - từ user3 (6 giờ trước)
        Post post9 = Post.builder()
                .userId(user3)
                .content("Coffee time ☕️ Anyone else needs caffeine to survive Monday?")
                .mediaUrl("https://images.unsplash.com/photo-1495474472287-4d71bcdd2085")
                .likeCount(0)
                .commentCount(0)
                .shareCount(0)
                .createdBy(user3)
                .lastUpdatedBy(user3)
                .publicity(2)
                .build();
        post9.setCreatedDate(Instant.now().minus(6, ChronoUnit.HOURS));
        post9.setModifiedDate(Instant.now().minus(6, ChronoUnit.HOURS));
        post9 = postRepository.save(post9);
        posts.add(post9);

        // Post 10 - từ user1 (2 giờ trước)
        Post post10 = Post.builder()
                .userId(user1)
                .content("Chia sẻ một số tips học tập hiệu quả: 1) Tập trung 2) Thực hành 3) Kiên trì 💡")
                .mediaUrl(null)
                .likeCount(0)
                .commentCount(0)
                .shareCount(0)
                .createdBy(user1)
                .lastUpdatedBy(user1)
                .publicity(2)
                .build();
        post10.setCreatedDate(Instant.now().minus(2, ChronoUnit.HOURS));
        post10.setModifiedDate(Instant.now().minus(2, ChronoUnit.HOURS));
        post10 = postRepository.save(post10);
        posts.add(post10);

        // ========== TẠO LIKES ==========
        List<Like> likes = new ArrayList<>();

        // User2 và User3 like Post1
        likes.add(Like.builder()
                .postId(post1.getId())
                .userId(user2)
                .createdDate(baseTime.plus(3, ChronoUnit.HOURS))
                .build());
        likes.add(Like.builder()
                .postId(post1.getId())
                .userId(user3)
                .createdDate(baseTime.plus(5, ChronoUnit.HOURS))
                .build());

        // User1 và User3 like Post2
        likes.add(Like.builder()
                .postId(post2.getId())
                .userId(user1)
                .createdDate(baseTime.plus(1, ChronoUnit.DAYS).plus(2, ChronoUnit.HOURS))
                .build());
        likes.add(Like.builder()
                .postId(post2.getId())
                .userId(user3)
                .createdDate(baseTime.plus(1, ChronoUnit.DAYS).plus(4, ChronoUnit.HOURS))
                .build());

        // User1 và User2 like Post3
        likes.add(Like.builder()
                .postId(post3.getId())
                .userId(user1)
                .createdDate(baseTime.plus(2, ChronoUnit.DAYS).plus(1, ChronoUnit.HOURS))
                .build());
        likes.add(Like.builder()
                .postId(post3.getId())
                .userId(user2)
                .createdDate(baseTime.plus(2, ChronoUnit.DAYS).plus(3, ChronoUnit.HOURS))
                .build());

        // User2 like Post4
        likes.add(Like.builder()
                .postId(post4.getId())
                .userId(user2)
                .createdDate(baseTime.plus(3, ChronoUnit.DAYS).plus(2, ChronoUnit.HOURS))
                .build());

        // User3 like Post5
        likes.add(Like.builder()
                .postId(post5.getId())
                .userId(user3)
                .createdDate(baseTime.plus(4, ChronoUnit.DAYS).plus(1, ChronoUnit.HOURS))
                .build());

        likeRepository.saveAll(likes);

        // ========== TẠO COMMENTS ==========
        List<Comment> comments = new ArrayList<>();

        // Comments cho Post1
        Comment comment1 = Comment.builder()
                .postId(post1.getId())
                .userId(user2)
                .content("Chào mừng bạn! Rất vui được gặp bạn ở đây! 🎊")
                .parentCommentId(null)
                .createdDate(baseTime.plus(1, ChronoUnit.HOURS))
                .modifiedDate(baseTime.plus(1, ChronoUnit.HOURS))
                .createdBy(user2)
                .lastUpdatedBy(user2)
                .publicity(2)
                .build();
        comment1 = commentRepository.save(comment1);

        // Reply cho comment1
        Comment reply1 = Comment.builder()
                .postId(post1.getId())
                .userId(user1)
                .content("Cảm ơn bạn nhiều! 😊")
                .parentCommentId(comment1.getId())
                .createdDate(baseTime.plus(2, ChronoUnit.HOURS))
                .modifiedDate(baseTime.plus(2, ChronoUnit.HOURS))
                .createdBy(user1)
                .lastUpdatedBy(user1)
                .publicity(2)
                .build();
        comments.add(reply1);

        Comment comment2 = Comment.builder()
                .postId(post1.getId())
                .userId(user3)
                .content("Nice to meet you! Welcome aboard! 👋")
                .parentCommentId(null)
                .createdDate(baseTime.plus(3, ChronoUnit.HOURS))
                .modifiedDate(baseTime.plus(3, ChronoUnit.HOURS))
                .createdBy(user3)
                .lastUpdatedBy(user3)
                .publicity(2)
                .build();
        comments.add(comment2);

        // Comments cho Post2
        Comment comment3 = Comment.builder()
                .postId(post2.getId())
                .userId(user1)
                .content("Thời tiết đẹp thật! Tôi định đi picnic với gia đình 🌳")
                .parentCommentId(null)
                .createdDate(baseTime.plus(1, ChronoUnit.DAYS).plus(1, ChronoUnit.HOURS))
                .modifiedDate(baseTime.plus(1, ChronoUnit.DAYS).plus(1, ChronoUnit.HOURS))
                .createdBy(user1)
                .lastUpdatedBy(user1)
                .publicity(2)
                .build();
        comment3 = commentRepository.save(comment3);

        Comment reply2 = Comment.builder()
                .postId(post2.getId())
                .userId(user2)
                .content("Tuyệt vời! Chúc bạn vui vẻ nhé! 🎉")
                .parentCommentId(comment3.getId())
                .createdDate(baseTime.plus(1, ChronoUnit.DAYS).plus(2, ChronoUnit.HOURS))
                .modifiedDate(baseTime.plus(1, ChronoUnit.DAYS).plus(2, ChronoUnit.HOURS))
                .createdBy(user2)
                .lastUpdatedBy(user2)
                .publicity(2)
                .build();
        comments.add(reply2);

        // Comments cho Post3
        Comment comment5 = Comment.builder()
                .postId(post3.getId())
                .userId(user1)
                .content("Great content! Very informative 👍")
                .parentCommentId(null)
                .createdDate(baseTime.plus(2, ChronoUnit.DAYS).plus(30, ChronoUnit.MINUTES))
                .modifiedDate(baseTime.plus(2, ChronoUnit.DAYS).plus(30, ChronoUnit.MINUTES))
                .createdBy(user1)
                .lastUpdatedBy(user1)
                .publicity(2)
                .build();
        comments.add(comment5);

        Comment comment6 = Comment.builder()
                .postId(post3.getId())
                .userId(user2)
                .content("I learned a lot from this. Thanks for sharing! 💯")
                .parentCommentId(null)
                .createdDate(baseTime.plus(2, ChronoUnit.DAYS).plus(2, ChronoUnit.HOURS))
                .modifiedDate(baseTime.plus(2, ChronoUnit.DAYS).plus(2, ChronoUnit.HOURS))
                .createdBy(user2)
                .lastUpdatedBy(user2)
                .publicity(2)
                .build();
        comments.add(comment6);

        // Comments cho Post4
        Comment comment7 = Comment.builder()
                .postId(post4.getId())
                .userId(user3)
                .content("Đúng vậy! Reflection là quan trọng 🤔")
                .parentCommentId(null)
                .createdDate(baseTime.plus(3, ChronoUnit.DAYS).plus(1, ChronoUnit.HOURS))
                .modifiedDate(baseTime.plus(3, ChronoUnit.DAYS).plus(1, ChronoUnit.HOURS))
                .createdBy(user3)
                .lastUpdatedBy(user3)
                .publicity(2)
                .build();
        comments.add(comment7);

        // Comments cho Post6
        Comment comment4 = Comment.builder()
                .postId(post6.getId())
                .userId(user1)
                .content("Tôi vote cho Flutter! Cross-platform và performance tốt 🚀")
                .parentCommentId(null)
                .createdDate(baseTime.plus(5, ChronoUnit.DAYS).plus(1, ChronoUnit.HOURS))
                .modifiedDate(baseTime.plus(5, ChronoUnit.DAYS).plus(1, ChronoUnit.HOURS))
                .createdBy(user1)
                .lastUpdatedBy(user1)
                .publicity(2)
                .build();
        comment4 = commentRepository.save(comment4);

        Comment comment8 = Comment.builder()
                .postId(post6.getId())
                .userId(user2)
                .content("React Native cũng không tệ đâu, có native module mạnh mẽ!")
                .parentCommentId(null)
                .createdDate(baseTime.plus(5, ChronoUnit.DAYS).plus(2, ChronoUnit.HOURS))
                .modifiedDate(baseTime.plus(5, ChronoUnit.DAYS).plus(2, ChronoUnit.HOURS))
                .createdBy(user2)
                .lastUpdatedBy(user2)
                .publicity(2)
                .build();
        comments.add(comment8);

        Comment reply3 = Comment.builder()
                .postId(post6.getId())
                .userId(user3)
                .content("Cả hai đều tốt, tùy use case thôi 😄")
                .parentCommentId(comment4.getId())
                .createdDate(baseTime.plus(5, ChronoUnit.DAYS).plus(3, ChronoUnit.HOURS))
                .modifiedDate(baseTime.plus(5, ChronoUnit.DAYS).plus(3, ChronoUnit.HOURS))
                .createdBy(user3)
                .lastUpdatedBy(user3)
                .publicity(2)
                .build();
        comments.add(reply3);

        commentRepository.saveAll(comments);

        // ========== TẠO SHARES ==========
        List<Share> shares = new ArrayList<>();

        // User2 share Post3
        shares.add(Share.builder()
                .postId(post3.getId())
                .userId(user2)
                .createdDate(baseTime.plus(2, ChronoUnit.DAYS).plus(4, ChronoUnit.HOURS))
                .build());

        // User3 share Post6
        shares.add(Share.builder()
                .postId(post6.getId())
                .userId(user3)
                .createdDate(baseTime.plus(5, ChronoUnit.DAYS).plus(4, ChronoUnit.HOURS))
                .build());

        // User1 share Post8
        shares.add(Share.builder()
                .postId(post8.getId())
                .userId(user1)
                .createdDate(Instant.now().minus(10, ChronoUnit.HOURS))
                .build());

        shareRepository.saveAll(shares);

        // ========== THỐNG KÊ ==========
        long totalPosts = postRepository.count();
        long totalLikes = likeRepository.count();
        long totalComments = commentRepository.count();
        long totalShares = shareRepository.count();

        String summary = String.format(
                "✅ Seed data thành công!\n" +
                        "📝 Posts: %d\n" +
                        "❤️ Likes: %d\n" +
                        "💬 Comments: %d (bao gồm replies)\n" +
                        "🔄 Shares: %d\n\n" +
                        "User IDs để test:\n" +
                        "- User 1: %s\n" +
                        "- User 2: %s\n" +
                        "- User 3: %s",
                totalPosts, totalLikes, totalComments, totalShares, user1, user2, user3
        );

        return ApiResponse.<String>builder()
                .message(summary)
                .result("Success")
                .build();
    }
}
