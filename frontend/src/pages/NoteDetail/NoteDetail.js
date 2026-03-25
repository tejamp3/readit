import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import API from "../../api/axios";
import { useAuth } from "../../context/AuthContext";
import VoteButtons from "../../components/VoteButtons/VoteButtons";
import CommentSection from "../../components/CommentSection/CommentSection";
import styles from "./NoteDetail.module.css";

function NoteDetail() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { user } = useAuth();

  const [note, setNote] = useState(null);
  const [comments, setComments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    fetchNote();
    fetchComments();
    // eslint-disable-next-line
  }, [id]);

  async function fetchNote() {
    try {
      const res = await API.get(`/notes/${id}`);
      setNote(res.data);
    } catch (err) {
      setError("Note not found.");
    } finally {
      setLoading(false);
    }
  }

  async function fetchComments() {
    try {
      const res = await API.get(`/notes/${id}/comments`);
      setComments(res.data);
    } catch (err) {
      // silently fail
    }
  }

  async function handleDelete() {
    if (!window.confirm("Are you sure you want to delete this note?")) return;
    try {
      await API.delete(`/notes/${id}`);
      navigate("/notes");
    } catch (err) {
      alert("Failed to delete note.");
    }
  }

  if (loading) return <div className={styles.center}>Loading...</div>;
  if (error) return <div className={styles.center}>{error}</div>;
  if (!note) return null;

  return (
    <div className={styles.wrapper}>
      <div className={styles.card}>
        {/* Back */}
        <button className={styles.backBtn} onClick={() => navigate("/notes")}>
          ← Back to Notes
        </button>

        {/* Subject badge */}
        <span className={styles.subject}>{note.subject}</span>

        <h1 className={styles.title}>{note.title}</h1>

        <div className={styles.meta}>
          <span>👤 {note.uploaderUsername}</span>
          <span>
            {new Date(note.createdAt).toLocaleDateString("en-IN", {
              day: "numeric", month: "long", year: "numeric",
            })}
          </span>
        </div>

        {note.description && (
          <p className={styles.description}>{note.description}</p>
        )}

        {/* Download button */}
        <div className={styles.downloadRow}>
          <span className={styles.fileName}>📄 {note.fileName}</span>
          <a
            href={`http://localhost:8080/uploads/notes/${note.fileName}`}
            target="_blank"
            rel="noreferrer"
            className={styles.downloadBtn}
          >
            ⬇ Download File
          </a>
        </div>

        {/* Votes + Delete */}
        <div className={styles.voteRow}>
          <VoteButtons
            type="note"
            id={note.id}
            initialUpvotes={note.upvotes}
            initialDownvotes={note.downvotes}
          />
          {user && user.username === note.uploaderUsername && (
            <button className={styles.deleteBtn} onClick={handleDelete}>
              🗑 Delete Note
            </button>
          )}
        </div>

        {/* Comments */}
        <CommentSection
          type="note"
          targetId={note.id}
          comments={comments}
          onCommentAdded={fetchComments}
        />
      </div>
    </div>
  );
}

export default NoteDetail;