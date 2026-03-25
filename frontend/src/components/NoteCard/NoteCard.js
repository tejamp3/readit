import React from "react";
import { useNavigate } from "react-router-dom";
import styles from "./NoteCard.module.css";

function NoteCard({ note }) {
  const navigate = useNavigate();

  return (
    <div className={styles.card} onClick={() => navigate(`/notes/${note.id}`)}>
      <div className={styles.topRow}>
        <span className={styles.subject}>{note.subject}</span>
        <span className={styles.author}>👤 {note.uploaderUsername}</span>
      </div>

      <h2 className={styles.title}>{note.title}</h2>

      {note.description && (
        <p className={styles.description}>
          {note.description.length > 120
            ? note.description.substring(0, 120) + "..."
            : note.description}
        </p>
      )}

      <div className={styles.footer}>
        <div className={styles.votes}>
          <span className={styles.upvote}>▲ {note.upvotes}</span>
          <span className={styles.downvote}>▼ {note.downvotes}</span>
        </div>
        <div className={styles.right}>
          <span className={styles.fileName}>📄 {note.fileName}</span>
          <span className={styles.date}>
            {new Date(note.createdAt).toLocaleDateString("en-IN", {
              day: "numeric", month: "short", year: "numeric",
            })}
          </span>
        </div>
      </div>
    </div>
  );
}

export default NoteCard;