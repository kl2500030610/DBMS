import { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

function Login() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const [UserStatus, setUserStatus] = useState(0);
  const [UserRole, setUserRole] = useState(0);
  const [UserPendingTask, setUserPendingTask] = useState(0);

  const navigate = useNavigate();

  const loginAndFetch = async () => {
    try {
      const response = await axios.post(
        "http://127.0.0.1:8000/login",
        {
          username: username,
          password: password
        }
      );

      console.log("Login:", response.data);

      setUserPendingTask(response.data.UserPendingTask);
      setUserRole(response.data.UserRole);
      setUserStatus(response.data.UserStatus);

      // ✅ OPTIONAL: redirect if login success
      if (response.data.UserStatus === 1) {
        alert("Login Successful");
        navigate("/"); // go back to dashboard
      } else {
        alert("Invalid credentials");
      }

    } catch (error) {
      console.error("Error:", error);
      alert("Server error");
    }
  };

  return (
    <div style={{ padding: "20px" }}>
      <h1>Login Page</h1>

      <input
        type="text"
        placeholder="Username"
        onChange={(e) => setUsername(e.target.value)}
      />
      <br /><br />

      <input
        type="password"
        placeholder="Password"
        onChange={(e) => setPassword(e.target.value)}
      />
      <br /><br />

      <button onClick={loginAndFetch}>Login</button>

      <br /><br />

      <p>Status: {UserStatus}</p>
      <p>Role: {UserRole}</p>
      <p>Pending Tasks: {UserPendingTask}</p>
    </div>
  );
}

export default Login;