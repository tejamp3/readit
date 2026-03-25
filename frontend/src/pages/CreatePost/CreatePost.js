import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import API from "../../api/axios";
import styles from "./CreatePost.module.css";

const CATEGORIES = [
  { value: "INTERVIEW_EXPERIENCE", label: "Interview Experience" },
  { value: "CAREER_ADVICE", label: "Career Advice" },
  { value: "GENERAL", label: "General" },
];

function CreatePost() {
  const navigate = useNavigate();
  const [form, setForm] = useState({ title: "", content: "", category: "GENERAL" });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  function handleChange(e) {
    setForm({ ...form, [e.target.name]: e.target.value });
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setError("");

    if (form.title.trim().length < 5) {
      setError("Title must be at least 5 characters");
      return;
    }
    if (form.content.trim().length === 0) {
      setError("Content cannot be empty");
      return;
    }

    setLoading(true);
    try {
      const res = await API.post("/posts", form);
      navigate(`/posts/${res.data.id}`);
    } catch (err) {
      setError(err.response?.data?.message || "Failed to create post");
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className={styles.wrapper}>
      <div className={styles.card}>
        <h2 className={styles.heading}>Create a Post</h2>

        {error && <div className={styles.error}>{error}</div>}

        <form onSubmit={handleSubmit} className={styles.form}>
          <div className={styles.field}>
            <label className={styles.label}>Category</label>
            <select
              className={styles.select}
              name="category"
              value={form.category}
              onChange={handleChange}
            >
              {CATEGORIES.map((cat) => (
                <option key={cat.value} value={cat.value}>{cat.label}</option>
              ))}
            </select>
          </div>

          <div className={styles.field}>
            <label className={styles.label}>Title</label>
            <input
              className={styles.input}
              type="text"
              name="title"
              value={form.title}
              onChange={handleChange}
              placeholder="Give your post a title..."
              maxLength={200}
              required
            />
            <span className={styles.charCount}>{form.title.length}/200</span>
          </div>

          <div className={styles.field}>
            <label className={styles.label}>Content</label>
            <textarea
              className={styles.textarea}
              name="content"
              value={form.content}
              onChange={handleChange}
              placeholder="Share your experience or thoughts..."
              rows={10}
              required
            />
          </div>

          <div className={styles.actions}>
            <button
              type="button"
              className={styles.cancelBtn}
              onClick={() => navigate("/")}
            >
              Cancel
            </button>
            <button
              type="submit"
              className={styles.submitBtn}
              disabled={loading}
            >
              {loading ? "Posting..." : "Post"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default CreatePost;