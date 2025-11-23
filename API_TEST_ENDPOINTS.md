# 🚀 API Testing Endpoints - Post Service

## Base URL
```
http://localhost:5004/post
```

---

## 📝 POST ENDPOINTS

### 1. Seed Data (Tạo dữ liệu mẫu)
```http
POST http://localhost:5004/post/seed

# Response:
{
  "code": 1000,
  "message": "✅ Seed data thành công!
📝 Posts: 10
❤️ Likes: 8
💬 Comments: 13
🔄 Shares: 3",
  "result": "Success"
}
```

### 2. Lấy tất cả posts (có phân trang)
```http
GET http://localhost:5004/post
GET http://localhost:5004/post?page=1&size=10

# Response:
{
  "code": 1000,
  "result": {
    "currentPage": 1,
    "totalPages": 1,
    "pageSize": 10,
    "totalElements": 10,
    "data": [
      {
        "id": "...",
        "content": "Chào mọi người!...",
        "mediaUrl": "https://...",
        "userId": "304af409-1bae-44a8-a777-0c41327854c2",
        "username": null,
        "created": "7 day(s) ago",
        "createdDate": "2025-11-04T09:00:00.000Z",
        "modifiedDate": "2025-11-04T09:00:00.000Z",
        "likeCount": 2,
        "commentCount": 3,
        "shareCount": 0,
        "isLiked": false,
        "isShared": false
      }
    ]
  }
}
```

### 3. Lấy post theo ID
```http
GET http://localhost:5004/post/{postId}
# Ví dụ:
GET http://localhost:5004/post/6912fc0bf0b8f6782c8e71eb
```

### 4. Tạo post mới
```http
POST http://localhost:5004/post
Content-Type: application/json

{
  "content": "Đây là bài post mới của tôi! 🎉",
  "mediaUrl": "https://images.unsplash.com/photo-1516117172878-fd2c41f4a759"
}

# Response:
{
  "code": 1000,
  "result": {
    "id": "...",
    "content": "Đây là bài post mới của tôi! 🎉",
    "mediaUrl": "...",
    "userId": "304af409-1bae-44a8-a777-0c41327854c2",
    "likeCount": 0,
    "commentCount": 0,
    "shareCount": 0
  }
}
```

### 5. Cập nhật post
```http
PUT http://localhost:5004/post/{postId}
Content-Type: application/json

{
  "content": "Nội dung đã được cập nhật! ✨",
  "mediaUrl": "https://images.unsplash.com/new-photo.jpg"
}
```

### 6. Xóa post
```http
DELETE http://localhost:5004/post/{postId}

# Response:
{
  "code": 1000,
  "message": "Post deleted successfully"
}
```

### 7. Lấy posts của user hiện tại
```http
GET http://localhost:5004/post/my-posts
GET http://localhost:5004/post/my-posts?page=1&size=10
```

### 8. Lấy posts theo userId
```http
GET http://localhost:5004/post/user/{userId}/posts
# Ví dụ:
GET http://localhost:5004/post/user/304af409-1bae-44a8-a777-0c41327854c2/posts?page=1&size=10
```

---

## ❤️ LIKE ENDPOINTS

### 9. Like một post
```http
POST http://localhost:5004/post/{postId}/like
# Ví dụ:
POST http://localhost:5004/post/6912fc0bf0b8f6782c8e71eb/like

# Response:
{
  "code": 1000,
  "message": "Post liked successfully"
}
```

### 10. Unlike một post
```http
DELETE http://localhost:5004/post/{postId}/like
# Ví dụ:
DELETE http://localhost:5004/post/6912fc0bf0b8f6782c8e71eb/like

# Response:
{
  "code": 1000,
  "message": "Post unliked successfully"
}
```

---

## 💬 COMMENT ENDPOINTS

### 11. Tạo comment cho post
```http
POST http://localhost:5004/post/{postId}/comment
Content-Type: application/json

{
  "content": "Bài viết rất hay! 👍",
  "parentCommentId": null
}

# Response:
{
  "code": 1000,
  "result": {
    "id": "...",
    "postId": "...",
    "userId": "...",
    "username": null,
    "content": "Bài viết rất hay! 👍",
    "parentCommentId": null,
    "created": "1 second(s) ago",
    "createdDate": "2025-11-11T09:30:00.000Z",
    "replies": []
  }
}
```

### 12. Reply một comment (trả lời comment)
```http
POST http://localhost:5004/post/{postId}/comment
Content-Type: application/json

{
  "content": "Cảm ơn bạn! 😊",
  "parentCommentId": "comment_id_cha"
}
```

### 13. Lấy danh sách comments của post
```http
GET http://localhost:5004/post/{postId}/comments
GET http://localhost:5004/post/{postId}/comments?page=1&size=10
# Ví dụ:
GET http://localhost:5004/post/6912fc0bf0b8f6782c8e71eb/comments

# Response:
{
  "code": 1000,
  "result": {
    "currentPage": 1,
    "totalPages": 1,
    "pageSize": 10,
    "totalElements": 3,
    "data": [
      {
        "id": "...",
        "postId": "...",
        "userId": "405bf509-2cbf-55b9-b888-1d52438965d3",
        "username": null,
        "content": "Chào mừng bạn! 🎊",
        "parentCommentId": null,
        "created": "7 day(s) ago",
        "replies": [
          {
            "id": "...",
            "content": "Cảm ơn bạn nhiều! 😊",
            "userId": "304af409-1bae-44a8-a777-0c41327854c2",
            "parentCommentId": "...",
            "created": "7 day(s) ago"
          }
        ]
      }
    ]
  }
}
```

### 14. Xóa comment
```http
DELETE http://localhost:5004/post/comment/{commentId}
# Ví dụ:
DELETE http://localhost:5004/post/comment/6912fc0bf0b8f6782c8e71fd

# Response:
{
  "code": 1000,
  "message": "Comment deleted successfully"
}
```

---

## 🔄 SHARE ENDPOINTS

### 15. Share một post
```http
POST http://localhost:5004/post/{postId}/share
# Ví dụ:
POST http://localhost:5004/post/6912fc0bf0b8f6782c8e71eb/share

# Response:
{
  "code": 1000,
  "message": "Post shared successfully"
}
```

### 16. Unshare một post
```http
DELETE http://localhost:5004/post/{postId}/share
# Ví dụ:
DELETE http://localhost:5004/post/6912fc0bf0b8f6782c8e71eb/share

# Response:
{
  "code": 1000,
  "message": "Post unshared successfully"
}
```

---

## 🧪 TEST SCENARIOS (Các kịch bản test)

### Scenario 1: Tạo post mới và tương tác
```http
# Bước 1: Tạo post
POST http://localhost:5004/post
Content-Type: application/json

{
  "content": "Test post mới! 🚀",
  "mediaUrl": null
}

# Lấy postId từ response, ví dụ: "abc123"

# Bước 2: Like post
POST http://localhost:5004/post/abc123/like

# Bước 3: Comment vào post
POST http://localhost:5004/post/abc123/comment
Content-Type: application/json

{
  "content": "Comment đầu tiên!",
  "parentCommentId": null
}

# Bước 4: Share post
POST http://localhost:5004/post/abc123/share

# Bước 5: Xem post với tất cả thông tin
GET http://localhost:5004/post/abc123
```

### Scenario 2: Comment và Reply
```http
# Bước 1: Tạo comment gốc
POST http://localhost:5004/post/{postId}/comment
Content-Type: application/json

{
  "content": "Comment cha",
  "parentCommentId": null
}

# Lấy commentId từ response, ví dụ: "comment123"

# Bước 2: Reply comment
POST http://localhost:5004/post/{postId}/comment
Content-Type: application/json

{
  "content": "Reply cho comment cha",
  "parentCommentId": "comment123"
}

# Bước 3: Xem tất cả comments với nested replies
GET http://localhost:5004/post/{postId}/comments
```

### Scenario 3: Unlike và Unshare
```http
# Bước 1: Like post
POST http://localhost:5004/post/{postId}/like

# Bước 2: Check post (isLiked = true)
GET http://localhost:5004/post/{postId}

# Bước 3: Unlike post
DELETE http://localhost:5004/post/{postId}/like

# Bước 4: Check lại (isLiked = false)
GET http://localhost:5004/post/{postId}
```

---

## 📊 USER IDs để test

Từ dữ liệu seed, có 3 users:

```
User 1: 304af409-1bae-44a8-a777-0c41327854c2
User 2: 405bf509-2cbf-55b9-b888-1d52438965d3
User 3: 506cg609-3dcg-66c0-c999-2e63549076e4
```

---

## ⚠️ LƯU Ý QUAN TRỌNG

### Để seed data CHÍNH XÁC:

**Bước 1:** Comment `@EnableMongoAuditing` trong `PostServiceApplication.java`:
```java
// @EnableMongoAuditing
```

**Bước 2:** Comment `@Component` trong `AuditListener.java`:
```java
// @Component
```

**Bước 3:** Restart ứng dụng

**Bước 4:** Gọi seed endpoint:
```http
POST http://localhost:5004/post/seed
```

**Bước 5:** Sau khi seed xong, muốn dùng auditing thì bỏ comment lại cả 2 chỗ

---

## 🎯 Quick Test Commands (Copy & Paste)

### Test Like
```bash
# Like
curl -X POST http://localhost:5004/post/{postId}/like

# Unlike
curl -X DELETE http://localhost:5004/post/{postId}/like
```

### Test Comment
```bash
# Create comment
curl -X POST http://localhost:5004/post/{postId}/comment \
  -H "Content-Type: application/json" \
  -d '{"content":"Test comment","parentCommentId":null}'

# Get comments
curl http://localhost:5004/post/{postId}/comments

# Delete comment
curl -X DELETE http://localhost:5004/post/comment/{commentId}
```

### Test Share
```bash
# Share
curl -X POST http://localhost:5004/post/{postId}/share

# Unshare
curl -X DELETE http://localhost:5004/post/{postId}/share
```

---

## 🐛 Troubleshooting

### Lỗi: "You already liked this post"
→ Bạn đã like post này rồi, gọi unlike trước

### Lỗi: "You haven't liked this post"
→ Bạn chưa like post này, không thể unlike

### Lỗi: "Post not found"
→ PostId không tồn tại, check lại ID

### Lỗi: "Comment not found"
→ CommentId không tồn tại hoặc đã bị xóa

### Dữ liệu bị UNKNOWN_USER
→ Cần tắt `@EnableMongoAuditing` và `@Component` trong AuditListener

---

## 📱 Sử dụng với Postman/Thunder Client

Import các endpoints trên vào Postman hoặc Thunder Client extension trong VS Code để test dễ dàng hơn.

**Collection Structure:**
```
Post Service
├── Posts
│   ├── Seed Data
│   ├── Get All Posts
│   ├── Get Post by ID
│   ├── Create Post
│   ├── Update Post
│   ├── Delete Post
│   ├── Get My Posts
│   └── Get Posts by User
├── Likes
│   ├── Like Post
│   └── Unlike Post
├── Comments
│   ├── Create Comment
│   ├── Create Reply
│   ├── Get Comments
│   └── Delete Comment
└── Shares
    ├── Share Post
    └── Unshare Post
```

Good luck testing! 🚀

