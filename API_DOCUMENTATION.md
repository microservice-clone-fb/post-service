# API Documentation - Post Service với Like, Comment, Share

## Base URL: http://localhost:5004/post

---

## POSTS API

### 1. Lấy tất cả posts (có phân trang)
```http
GET /post?page=1&size=10
```

### 2. Tạo post mới
```http
POST /post
Content-Type: application/json

{
  "content": "Nội dung bài post",
  "mediaUrl": "https://example.com/image.jpg"
}
```

### 3. Lấy post theo ID
```http
GET /post/{postId}
```

### 4. Cập nhật post
```http
PUT /post/{postId}
Content-Type: application/json

{
  "content": "Nội dung đã cập nhật",
  "mediaUrl": "https://example.com/new-image.jpg"
}
```

### 5. Xóa post
```http
DELETE /post/{postId}
```

### 6. Lấy posts của user hiện tại
```http
GET /post/my-posts?page=1&size=10
```

### 7. Lấy posts theo userId
```http
GET /post/user/{userId}/posts?page=1&size=10
```

---

## LIKE API

### 8. Like một post
```http
POST /post/{postId}/like
```

### 9. Unlike một post
```http
DELETE /post/{postId}/like
```

**Response của Post sẽ bao gồm:**
```json
{
  "code": 1000,
  "result": {
    "id": "674a1b2c3d4e5f6a7b8c9d0f",
    "content": "Nội dung post",
    "likeCount": 10,
    "isLiked": true
  }
}
```

---

## COMMENT API

### 10. Tạo comment cho post
```http
POST /post/{postId}/comment
Content-Type: application/json

{
  "content": "Đây là comment của tôi",
  "parentCommentId": null
}
```

### 11. Reply comment (trả lời comment)
```http
POST /post/{postId}/comment
Content-Type: application/json

{
  "content": "Đây là reply",
  "parentCommentId": "comment_id_cha"
}
```

### 12. Lấy danh sách comment của post
```http
GET /post/{postId}/comments?page=1&size=10
```

**Response:**
```json
{
  "code": 1000,
  "result": {
    "currentPage": 1,
    "totalPages": 1,
    "pageSize": 10,
    "totalElements": 5,
    "data": [
      {
        "id": "comment_id",
        "postId": "post_id",
        "userId": "user_id",
        "username": "john_doe",
        "content": "Đây là comment",
        "parentCommentId": null,
        "created": "2 hours ago",
        "createdDate": "2024-01-16T14:20:00.000Z",
        "replies": [
          {
            "id": "reply_id",
            "content": "Đây là reply",
            "parentCommentId": "comment_id",
            "username": "jane_doe",
            "created": "1 hour ago"
          }
        ]
      }
    ]
  }
}
```

### 13. Xóa comment
```http
DELETE /post/comment/{commentId}
```

---

## SHARE API

### 14. Share một post
```http
POST /post/{postId}/share
```

### 15. Unshare một post
```http
DELETE /post/{postId}/share
```

**Response của Post sẽ bao gồm:**
```json
{
  "code": 1000,
  "result": {
    "id": "674a1b2c3d4e5f6a7b8c9d0f",
    "content": "Nội dung post",
    "shareCount": 5,
    "isShared": true
  }
}
```

---

## POST RESPONSE MẪU (đầy đủ)

```json
{
  "code": 1000,
  "result": {
    "id": "674a1b2c3d4e5f6a7b8c9d0f",
    "content": "Hôm nay thời tiết thật đẹp! 🌞",
    "mediaUrl": "https://example.com/image.jpg",
    "userId": "304af409-1bae-44a8-a777-0c41327854c2",
    "username": "john_doe",
    "created": "2 hours ago",
    "createdDate": "2024-01-16T14:20:00.000Z",
    "modifiedDate": "2024-01-16T14:20:00.000Z",
    "likeCount": 10,
    "commentCount": 5,
    "shareCount": 3,
    "isLiked": true,
    "isShared": false
  }
}
```

---

## LƯU Ý

1. **Tất cả các endpoint đều yêu cầu authentication** (trừ khi đã tắt security như hiện tại)
2. **User ID** được lấy từ JWT token hoặc security context
3. **Phân trang** mặc định: page=1, size=10
4. **Sort** mặc định: theo createdDate giảm dần (mới nhất trước)
5. **Comment có thể nested**: parentCommentId = null là comment gốc, có giá trị là reply

---

## DATABASE COLLECTIONS

- **post**: Lưu bài post
- **comment**: Lưu comment và reply
- **like**: Lưu like của user cho post
- **share**: Lưu share của user cho post

