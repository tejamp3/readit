import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import API from "../../api/axios";
import { useAuth } from "../../context/AuthContext";
import VoteButtons from "../../components/VoteButtons/VoteButtons";
import CommentSection from "../../components/CommentSection/CommentSection";
import styles from "./PostDetail.module.css";

const CATEGORY_LABELS = {
  INTERVIEW_EXPERIENCE: "Interview Experience",
  CAREER_ADVICE: "Career Advice",
  GENERAL: "General",
};

function PostDetail() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { user } = useAuth();

  const [post, setPost] = useState(null);
  const [comments, setComments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    fetchPost();
    fetchComments();
    // eslint-disable-next-line
  }, [id]);

  async function fetchPost() {
    try {
      const res = await API.get(`/posts/${id}`);
      setPost(res.data);
    } catch (err) {
      setError("Post not found.");
    } finally {
      setLoading(false);
    }
  }

  async function fetchComments() {
    try {
      const res = await API.get(`/posts/${id}/comments`);
      setComments(res.data);
    } catch (err) {
      // silently fail
    }
  }

  async function handleDelete() {
    if (!window.confirm("Are you sure you want to delete this post?")) return;
    try {
      await API.delete(`/posts/${id}`);
      navigate("/");
    } catch (err) {
      alert("Failed to delete post.");
    }
  }

  if (loading) return <div className={styles.center}>Loading...</div>;
  if (error) return <div className={styles.center}>{error}</div>;
  if (!post) return null;

  return (
    <div className={styles.wrapper}>
      <div className={styles.card}>
        {/* Back button */}
        <button className={styles.backBtn} onClick={() => navigate("/")}>
          ← Back to Feed
        </button>

        {/* Category badge */}
        <span className={styles.category}>
          {CATEGORY_LABELS[post.category] || post.category}
        </span>

        <h1 className={styles.title}>{post.title}</h1>

        <div className={styles.meta}>
          <span>👤 {post.authorUsername}</span>
          <span>
            {new Date(post.createdAt).toLocaleDateString("en-IN", {
              day: "numeric", month: "long", year: "numeric"
            })}
          </span>
        </div>

        <div className={styles.content}>{post.content}</div>

        {/* Votes */}
        <div className={styles.voteRow}>
          <VoteButtons
            type="post"
            id={post.id}
            initialUpvotes={post.upvotes}
            initialDownvotes={post.downvotes}
          />

          {/* Delete — only for post owner */}
          {user && user.username === post.authorUsername && (
            <button className={styles.deleteBtn} onClick={handleDelete}>
              🗑 Delete Post
            </button>
          )}
        </div>

        {/* Comments */}
        <CommentSection
          type="post"
          targetId={post.id}
          comments={comments}
          onCommentAdded={fetchComments}
        />
      </div>
    </div>
  );
}

export default PostDetail;