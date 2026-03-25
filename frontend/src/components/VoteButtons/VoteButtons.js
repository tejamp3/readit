import React, { useState } from "react";
import API from "../../api/axios";
import styles from "./VoteButtons.module.css";

function VoteButtons({ type, id, initialUpvotes, initialDownvotes }) {
  const [upvotes, setUpvotes] = useState(initialUpvotes);
  const [downvotes, setDownvotes] = useState(initialDownvotes);
  const [loading, setLoading] = useState(false);

  async function handleVote(voteType) {
    if (loading) return;
    setLoading(true);
    try {
      const endpoint = type === "post"
        ? `/posts/${id}/vote`
        : `/notes/${id}/vote`;

      const res = await API.post(endpoint, { voteType });
      const message = res.data;

      // Optimistic UI update based on server response message
      if (voteType === "UPVOTE") {
        if (message === "Vote recorded") {
          setUpvotes((v) => v + 1);
        } else if (message === "Vote removed") {
          setUpvotes((v) => v - 1);
        } else if (message === "Vote updated") {
          setUpvotes((v) => v + 1);
          setDownvotes((v) => v - 1);
        }
      } else {
        if (message === "Vote recorded") {
          setDownvotes((v) => v + 1);
        } else if (message === "Vote removed") {
          setDownvotes((v) => v - 1);
        } else if (message === "Vote updated") {
          setDownvotes((v) => v + 1);
          setUpvotes((v) => v - 1);
        }
      }
    } catch (err) {
      // silently fail
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className={styles.voteContainer}>
      <button
        className={styles.upvoteBtn}
        onClick={() => handleVote("UPVOTE")}
        disabled={loading}
      >
        ▲ {upvotes}
      </button>
      <button
        className={styles.downvoteBtn}
        onClick={() => handleVote("DOWNVOTE")}
        disabled={loading}
      >
        ▼ {downvotes}
      </button>
    </div>
  );
}

export default VoteButtons;