import React from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import styles from "./Navbar.module.css";

function Navbar() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  function handleLogout() {
    logout();
    navigate("/login");
  }

  return (
    <nav className={styles.navbar}>
      <div className={styles.left}>
        <Link to="/" className={styles.logo}>
          📖 ReadIt
        </Link>
        {user && (
          <div className={styles.navLinks}>
            <Link to="/" className={styles.navLink}>Feed</Link>
            <Link to="/notes" className={styles.navLink}>Notes</Link>
          </div>
        )}
      </div>

      <div className={styles.right}>
        {user ? (
          <>
            <span className={styles.username}>👤 {user.username}</span>
            <Link to="/posts/create" className={styles.btnPrimary}>+ Post</Link>
            <Link to="/notes/upload" className={styles.btnSecondary}>+ Note</Link>
            <button onClick={handleLogout} className={styles.btnLogout}>Logout</button>
          </>
        ) : (
          <>
            <Link to="/login" className={styles.btnSecondary}>Login</Link>
            <Link to="/register" className={styles.btnPrimary}>Register</Link>
          </>
        )}
      </div>
    </nav>
  );
}

export default Navbar;