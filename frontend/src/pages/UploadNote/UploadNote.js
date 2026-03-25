import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import API from "../../api/axios";
import styles from "./UploadNote.module.css";

function UploadNote() {
  const navigate = useNavigate();
  const [form, setForm] = useState({ title: "", description: "", subject: "" });
  const [file, setFile] = useState(null);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  function handleChange(e) {
    setForm({ ...form, [e.target.name]: e.target.value });
  }

  function handleFileChange(e) {
    setFile(e.target.files[0]);
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setError("");

    if (!file) {
      setError("Please select a file to upload.");
      return;
    }

    if (form.title.trim().length < 3) {
      setError("Title must be at least 3 characters.");
      return;
    }

    const formData = new FormData();
    formData.append("title", form.title);
    formData.append("description", form.description);
    formData.append("subject", form.subject);
    formData.append("file", file);

    setLoading(true);
    try {
      const res = await API.post("/notes", formData, {
        headers: { "Content-Type": "multipart/form-data" },
      });
      navigate(`/notes/${res.data.id}`);
    } catch (err) {
      setError(err.response?.data?.message || "Failed to upload note.");
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className={styles.wrapper}>
      <div className={styles.card}>
        <h2 className={styles.heading}>Upload Notes</h2>
        <p className={styles.subheading}>Share your study notes with the community.</p>

        {error && <div className={styles.error}>{error}</div>}

        <form onSubmit={handleSubmit} className={styles.form}>
          <div className={styles.field}>
            <label className={styles.label}>Title *</label>
            <input
              className={styles.input}
              type="text"
              name="title"
              value={form.title}
              onChange={handleChange}
              placeholder="e.g. Complete DSA Notes"
              maxLength={200}
              required
            />
          </div>

          <div className={styles.field}>
            <label className={styles.label}>Subject *</label>
            <input
              className={styles.input}
              type="text"
              name="subject"
              value={form.subject}
              onChange={handleChange}
              placeholder="e.g. Data Structures, OS, DBMS..."
              maxLength={100}
              required
            />
          </div>

          <div className={styles.field}>
            <label className={styles.label}>Description</label>
            <textarea
              className={styles.textarea}
              name="description"
              value={form.description}
              onChange={handleChange}
              placeholder="Brief description of what's covered..."
              rows={4}
              maxLength={500}
            />
            <span className={styles.charCount}>{form.description.length}/500</span>
          </div>

          <div className={styles.field}>
            <label className={styles.label}>File *</label>
            <div className={styles.fileWrapper}>
              <input
                className={styles.fileInput}
                type="file"
                id="fileUpload"
                onChange={handleFileChange}
                accept=".pdf,.doc,.docx,.ppt,.pptx,.txt"
              />
              <label htmlFor="fileUpload" className={styles.fileLabel}>
                {file ? `📄 ${file.name}` : "📁 Choose a file..."}
              </label>
            </div>
            <span className={styles.fileHint}>
              Supported: PDF, DOC, DOCX, PPT, PPTX, TXT
            </span>
          </div>

          <div className={styles.actions}>
            <button
              type="button"
              className={styles.cancelBtn}
              onClick={() => navigate("/notes")}
            >
              Cancel
            </button>
            <button
              type="submit"
              className={styles.submitBtn}
              disabled={loading}
            >
              {loading ? "Uploading..." : "Upload"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default UploadNote;