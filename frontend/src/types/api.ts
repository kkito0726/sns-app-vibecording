// src/types/api.ts

export interface UpdateUserProfileRequest {
  username?: string;
  bio?: string;
}

export interface UserResponse {
  id: number;
  username: string;
  email: string;
  profileImageUrl?: string;
  bio?: string;
  followerCount: number;
  followingCount: number;
  isFollowing: boolean;
}

export interface RegisterUserRequest {
  username: string;
  email: string;
  password?: string;
}

export interface FlickResponse {
  id: number;
  userId: number;
  textContent?: string;
  imageUrl?: string;
  videoUrl?: string;
  postType: 'TEXT' | 'IMAGE' | 'VIDEO';
  createdAt: string;
}

export interface CommentCreationRequest {
  text: string;
}

export interface CommentResponse {
  id: number;
  userId: number;
  flickId: number;
  text: string;
  createdAt: string;
  authorUsername: string;
  authorProfileImageUrl?: string;
}

export interface LoginUserRequest {
  email: string;
  password?: string;
}

export interface LoginResponse {
  token: string;
}

export interface AuthorInfo {
  userId: number;
  username: string;
  profileImageUrl?: string;
}

export interface FlickDetailResponse {
  id: number;
  textContent?: string;
  imageUrl?: string;
  videoUrl?: string;
  postType: 'TEXT' | 'IMAGE' | 'VIDEO';
  createdAt: string;
  author: AuthorInfo;
  likeCount: number;
  isLiked: boolean;
}
