import React, { useState } from "react";
import API from "../../api/axios";
import { useAuth } from "../../context/AuthContext";
import styles from "./CommentSection.module.css";

function CommentItem({ comment, type, targetId, onCommentAdded }) {
  const { user } = useAuth();
  const [showReplyBox, setShowReplyBox] = useState(false);
  const [replyContent, setReplyContent] = useState("");
  const [loading, setLoading] = useState(false);

  async function handleReplySubmit(e) {
    e.preventDefault();
    if (!replyContent.trim()) return;
    setLoading(true);
    try {
      const endpoint = type === "post"
        ? `/posts/${targetId}/comments`
        : `/notes/${targetId}/comments`;
      await API.post(endpoint, {
        content: replyContent,
        parentId: comment.id,
      });
      setReplyContent("");
      setShowReplyBox(false);
      onCommentAdded();
    } catch (err) {
      // silently fail
    } finally {
      setLoading(false);
    }
  }

  async function handleDelete() {
    if (!window.confirm("Delete this comment?")) return;
    try {
      await API.delete(`/comments/${comment.id}`);
      onCommentAdded(); // refresh
    } catch (err) {
      // silently fail
    }
  }

  return (
    <div className={styles.commentItem}>
      <div className={styles.commentHeader}>
        <span className={styles.commentAuthor}>👤 {comment.authorUsername}</span>
        <span className={styles.commentDate}>
          {new Date(comment.createdAt).toLocaleDateString("en-IN", {
            day: "numeric", month: "short", year: "numeric"
          })}
        </span>
      </div>
      <p className={styles.commentContent}>{comment.content}</p>

      <div className={styles.commentActions}>
        <button
          className={styles.replyBtn}
          onClick={() => setShowReplyBox(!showReplyBox)}
        >
          💬 Reply
        </button>
        {user && user.username === comment.authorUsername && (
          <button className={styles.deleteBtn} onClick={handleDelete}>
            🗑 Delete
          </button>
        )}
      </div>

      {showReplyBox && (
        <form onSubmit={handleReplySubmit} className={styles.replyForm}>
          <textarea
            className={styles.replyInput}
            value={replyContent}
            onChange={(e) => setReplyContent(e.target.value)}
            placeholder="Write a reply..."
            rows={2}
            required
          />
          <div className={styles.replyActions}>
            <button
              type="button"
              className={styles.cancelReplyBtn}
              onClick={() => setShowReplyBox(false)}
            >
              Cancel
            </button>
            <button
              type="submit"
              className={styles.submitReplyBtn}
              disabled={loading}
            >
              {loading ? "Posting..." : "Reply"}
            </button>
          </div>
        </form>
      )}

      {/* Render nested replies */}
      {comment.replies && comment.replies.length > 0 && (
        <div className={styles.replies}>
          {comment.replies.map((reply) => (
            <CommentItem
              key={reply.id}
              comment={reply}
              type={type}
              targetId={targetId}
              onCommentAdded={onCommentAdded}
            />
          ))}
        </div>
      )}
    </div>
  );
}

function CommentSection({ type, targetId, comments, onCommentAdded }) {
  const [content, setContent] = useState("");
  const [loading, setLoading] = useState(false);

  async function handleSubmit(e) {
    e.preventDefault();
    if (!content.trim()) return;
    setLoading(true);
    try {
      const endpoint = type === "post"
        ? `/posts/${targetId}/comments`
        : `/notes/${targetId}/comments`;
      await API.post(endpoint, { content, parentId: null });
      setContent("");
      onCommentAdded();
    } catch (err) {
      // silently fail
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className={styles.section}>
      <h3 className={styles.heading}>
        💬 Comments ({comments.length})
      </h3>

      {/* Add comment box */}
      <form onSubmit={handleSubmit} className={styles.addComment}>
        <textarea
          className={styles.input}
          value={content}
          onChange={(e) => setContent(e.target.value)}
          placeholder="Add a comment..."
          rows={3}
          required
        />
        <button
          type="submit"
          className={styles.submitBtn}
          disabled={loading}
        >
          {loading ? "Posting..." : "Comment"}
        </button>
      </form>

      {/* Comment list */}
      <div className={styles.commentList}>
        {comments.length === 0 && (
          <p className={styles.empty}>No comments yet. Start the conversation!</p>
        )}
        {comments.map((comment) => (
          <CommentItem
            key={comment.id}
            comment={comment}
            type={type}
            targetId={targetId}
            onCommentAdded={onCommentAdded}
          />
        ))}
      </div>
    </div>
  );
}

export default CommentSection;