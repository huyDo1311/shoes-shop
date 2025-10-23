import axios from "axios";
import config from "@/constants/config";
import { useContext } from "react";
import AuthContext from "@/context/AuthContext";

export default function useSignOut() {
  const context = useContext(AuthContext);

  if (!context) {
    throw new Error("useSignOut must be used within an AuthProvider");
  }

  const { deleteCurrentUser, setIsLoggedOut } = context;

  const signOut = async () => {
    deleteCurrentUser();
    setIsLoggedOut(true);
    const res = await axios.get(`${config.baseUrl}/sign-out`, {
      withCredentials: true,
    });
    return res.data;
  };

  return signOut;
}
