import React from "react";
import { useNavigate } from "react-router-dom";
import styles from "./PostCard.module.css";

function PostCard({ post }) {
  const navigate = useNavigate();

  const categoryColors = {
    INTERVIEW_EXPERIENCE: { bg: "#fff3e0", color: "#e65100", label: "Interview Experience" },
    CAREER_ADVICE: { bg: "#e8f5e9", color: "#2e7d32", label: "Career Advice" },
    GENERAL: { bg: "#e3f2fd", color: "#1565c0", label: "General" },
  };

  const cat = categoryColors[post.category] || categoryColors.GENERAL;

  return (
    <div className={styles.card} onClick={() => navigate(`/posts/${post.id}`)}>
      <div className={styles.topRow}>
        <span
          className={styles.category}
          style={{ background: cat.bg, color: cat.color }}
        >
          {cat.label}
        </span>
        <span className={styles.author}>👤 {post.authorUsername}</span>
      </div>

      <h2 className={styles.title}>{post.title}</h2>
      <p className={styles.preview}>
        {post.content.length > 150
          ? post.content.substring(0, 150) + "..."
          : post.content}
      </p>

      <div className={styles.footer}>
        <div className={styles.votes}>
          <span className={styles.upvote}>▲ {post.upvotes}</span>
          <span className={styles.downvote}>▼ {post.downvotes}</span>
        </div>
        <span className={styles.date}>
          {new Date(post.createdAt).toLocaleDateString("en-IN", {
            day: "numeric", month: "short", year: "numeric"
          })}
        </span>
      </div>
    </div>
  );
}

export default PostCard;