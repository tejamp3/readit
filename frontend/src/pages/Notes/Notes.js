import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import API from "../../api/axios";
import NoteCard from "../../components/NoteCard/NoteCard";
import styles from "./Notes.module.css";

function Notes() {
  const navigate = useNavigate();
  const [notes, setNotes] = useState([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(1);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [search, setSearch] = useState("");
  const [searchInput, setSearchInput] = useState("");

  useEffect(() => {
    fetchNotes();
    // eslint-disable-next-line
  }, [page]);

  async function fetchNotes() {
    setLoading(true);
    setError("");
    try {
      const res = await API.get(`/notes?page=${page}&size=10`);
      setNotes(res.data.content);
      setTotalPages(res.data.totalPages);
    } catch (err) {
      setError("Failed to load notes. Please try again.");
    } finally {
      setLoading(false);
    }
  }

  // Client-side subject filter
  const filteredNotes = search.trim()
    ? notes.filter((n) =>
        n.subject.toLowerCase().includes(search.toLowerCase()) ||
        n.title.toLowerCase().includes(search.toLowerCase())
      )
    : notes;

  function handleSearch(e) {
    e.preventDefault();
    setSearch(searchInput);
  }

  function handleClear() {
    setSearch("");
    setSearchInput("");
  }

  return (
    <div className={styles.wrapper}>
      {/* Sidebar */}
      <aside className={styles.sidebar}>
        <div className={styles.sideCard}>
          <h3 className={styles.sideTitle}>📚 Notes</h3>
          <p className={styles.sideDesc}>
            Browse and download study notes shared by the community.
          </p>
          <button
            className={styles.uploadBtn}
            onClick={() => navigate("/notes/upload")}
          >
            + Upload Notes
          </button>
        </div>
      </aside>

      {/* Main */}
      <main className={styles.main}>
        {/* Search bar */}
        <form onSubmit={handleSearch} className={styles.searchForm}>
          <input
            className={styles.searchInput}
            type="text"
            value={searchInput}
            onChange={(e) => setSearchInput(e.target.value)}
            placeholder="Search by title or subject..."
          />
          <button type="submit" className={styles.searchBtn}>Search</button>
          {search && (
            <button type="button" className={styles.clearBtn} onClick={handleClear}>
              Clear
            </button>
          )}
        </form>

        {search && (
          <p className={styles.searchInfo}>
            Showing results for "<strong>{search}</strong>"
          </p>
        )}

        {loading && <div className={styles.loading}>Loading notes...</div>}
        {error && <div className={styles.error}>{error}</div>}

        {!loading && !error && filteredNotes.length === 0 && (
          <div className={styles.empty}>
            {search ? "No notes match your search." : "No notes uploaded yet. Be the first!"}
          </div>
        )}

        <div className={styles.noteList}>
          {filteredNotes.map((note) => (
            <NoteCard key={note.id} note={note} />
          ))}
        </div>

        {/* Pagination — hide when searching */}
        {!search && totalPages > 1 && (
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

export default Notes;