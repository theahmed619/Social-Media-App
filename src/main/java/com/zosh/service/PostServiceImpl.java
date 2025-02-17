package com.zosh.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zosh.models.Post;
import com.zosh.models.User;
import com.zosh.repository.PostRepo;
import com.zosh.repository.UserRepo;

@Service
public class PostServiceImpl implements PostService{
	
	@Autowired
	PostRepo postRepo;
	
	@Autowired
	UserService userService;
	
	@Autowired
	UserRepo userRepo;

	@Override
	public Post createNewPost(Post post, Integer userId) throws Exception {

		User user=userService.findUserById(userId);
		Post newPost=new Post();
		newPost.setCaption(post.getCaption());
		newPost.setImage(post.getImage());
		newPost.setVideo(post.getVideo());
		newPost.setCreatedAt(LocalDateTime.now());
		
		newPost.setUser(user);
		
		return postRepo.save(newPost);
	}

	@Override
	public String deletePost(Integer postId, Integer userId) throws Exception  {
		
		Post post=findPostById(postId);
		User user=userService.findUserById(userId);
		
		if(post.getUser().getId()!=user.getId()) {
			throw new Exception("you can't delete another users post");
		}
		
		postRepo.delete(post);
		
		return "post deleted successfully";
	}

	@Override
	public List<Post> findPostByUserId(Integer userId) {
	
		return postRepo.findPostByUserId(userId);
	}

	@Override
	public Post findPostById(Integer postId) throws Exception {
		Optional<Post> opt=postRepo.findById(postId);
		if(opt.isEmpty()) {
			throw new Exception("post not found with id:"+postId);
		}
		
		return opt.get();
	}

	@Override
	public List<Post> findAllPost() {
		
		return postRepo.findAll();
	}

	@Override
	public Post savedPost(Integer postId, Integer userId) throws Exception {

		Post post=findPostById(postId);
		User user=userService.findUserById(userId);
		
		if(user.getSavedPost().contains(post)) {
			user.getSavedPost().remove(post);
		}
		else user.getSavedPost().add(post);
		userRepo.save(user);
		return post;
	}

	@Override
	public Post likePost(Integer postId, Integer userId) throws Exception {
		Post post=findPostById(postId);
		User user=userService.findUserById(userId);
		if(post.getLiked().contains(user)) {
			post.getLiked().remove(user);
		}else {
			post.getLiked().add(user);
		}
		
		return postRepo.save(post);
	}

}
