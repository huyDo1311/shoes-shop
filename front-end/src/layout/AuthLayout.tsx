import { Navigate, useLocation } from "react-router-dom";
import SettingLayout from "./SettingsLayout";
import { SidebarLink } from "@/components/ui/sidebar";
import { useAuth } from "@/hooks/useAuth";
import Unauthorized from "./Unauthorized";
import { useContext } from "react";
import AuthContext from "@/context/AuthContext";

const AuthLayout = ({
  allowedRole,
  navbar,
}: {
  allowedRole: string[];
  navbar: NavbarProps[];
}) => {
  const auth = useContext(AuthContext);

  const location = useLocation();

  return (
    <>
      {allowedRole.find((v) => auth?.currentUser?.role.includes(v)) ? (
        <SettingLayout>
          {navbar.map((link, idx) => {
            return <SidebarLink key={idx} link={link} />;
          })}
        </SettingLayout>
      ) : auth?.auth ? (
        <>
          <Unauthorized />
        </>
      ) : (
        <Navigate to="/sign-in" state={{ from: location }} replace />
      )}
    </>
  );
};

export default AuthLayout;
