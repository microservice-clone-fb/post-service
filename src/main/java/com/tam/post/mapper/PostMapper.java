package com.tam.post.mapper;

import com.tam.post.dto.response.PostResponse;
import com.tam.post.entity.Post;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostMapper {
    PostResponse toPostResponse(Post post);
}
