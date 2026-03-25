import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import API from "../../api/axios";
import PostCard from "../../components/Postcard/PostCard";
import styles from "./Home.module.css";

const CATEGORIES = ["ALL", "INTERVIEW_EXPERIENCE", "CAREER_ADVICE", "GENERAL"];

const CATEGORY_LABELS = {
  ALL: "All",
  INTERVIEW_EXPERIENCE: "Interview Experience",
  CAREER_ADVICE: "Career Advice",
  GENERAL: "General",
};

function Home() {
  const navigate = useNavigate();
  const [posts, setPosts] = useState([]);
  const [activeCategory, setActiveCategory] = useState("ALL");
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(1);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    fetchPosts();
    // eslint-disable-next-line
  }, [activeCategory, page]);

  async function fetchPosts() {
    setLoading(true);
    setError("");
    try {
      let res;
      if (activeCategory === "ALL") {
        res = await API.get(`/posts?page=${page}&size=10`);
      } else {
        res = await API.get(`/posts/category/${activeCategory}?page=${page}&size=10`);
      }
      setPosts(res.data.content);
      setTotalPages(res.data.totalPages);
    } catch (err) {
      setError("Failed to load posts. Please try again.");
    } finally {
      setLoading(false);
    }
  }

  function handleCategoryChange(cat) {
    setActiveCategory(cat);
    setPage(0);
  }

  return (
    <div className={styles.wrapper}>
      {/* Sidebar */}
      <aside className={styles.sidebar}>
        <div className={styles.sideCard}>
          <h3 className={styles.sideTitle}>📖 ReadIt</h3>
          <p className={styles.sideDesc}>
            A community for sharing interview experiences, career advice, and study notes.
          </p>
          <button
            className={styles.createBtn}
            onClick={() => navigate("/posts/create")}
          >
            + Create Post
          </button>
          <button
            className={styles.uploadBtn}
            onClick={() => navigate("/notes/upload")}
          >
            + Upload Notes
          </button>
        </div>
      </aside>

      {/* Main feed */}
      <main className={styles.feed}>
        {/* Category filter tabs */}
        <div className={styles.tabs}>
          {CATEGORIES.map((cat) => (
            <button
              key={cat}
              className={`${styles.tab} ${activeCategory === cat ? styles.activeTab : ""}`}
              onClick={() => handleCategoryChange(cat)}
            >
              {CATEGORY_LABELS[cat]}
            </button>
          ))}
        </div>

        {loading && <div className={styles.loading}>Loading posts...</div>}
        {error && <div className={styles.error}>{error}</div>}

        {!loading && !error && posts.length === 0 && (
          <div className={styles.empty}>No posts here yet. Be the first to post!</div>
        )}

        <div className={styles.postList}>
          {posts.map((post) => (
            <PostCard key={post.id} post={post} />
          ))}
        </div>

        {/* Pagination */}
        {totalPages > 1 && (
          <div className={styles.pagination}>
            <button
              className={styles.pageBtn}
              disabled={page === 0}
              onClick={() => setPage(page - 1)}
            >
              ← Prev
            </button>
            <span className={styles.pageInfo}>
              Page {page + 1} of {totalPages}
            </span>
            <button
              className={styles.pageBtn}
              disabled={page + 1 >= totalPages}
              onClick={() => setPage(page + 1)}
            >
              Next →
            </button>
          </div>
        )}
      </main>
    </div>
  );
}

export default Home;