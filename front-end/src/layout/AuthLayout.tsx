import { Navigate, Outlet, useLocation } from "react-router-dom";
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
  // console.log("auth", auth)

  const location = useLocation();

  //   const hasAccess = allowedRole.some((allowed) => {
  //   const userRole = auth?.currentUser?.role
  //   // console.log(userRole)
  //   if (!userRole) return false
  //   // hỗ trợ cả "ROLE_USER" và "USER"

  //   return userRole === allowed || userRole === allowed.replace("ROLE_", "")


  // })

  const userRole = auth?.currentUser?.role?.replace("ROLE_", "")
  const hasAccess = allowedRole.some(role => role.replace("ROLE_", "") === userRole)



  return (
    <>
      {/* {allowedRole.find((v) => auth?.currentUser?.role.includes(v)) ? (
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
      )} */}
      {hasAccess ? (
        <SettingLayout>
          {navbar.map((link, idx) => (
            <SidebarLink key={idx} link={link} />
          ))}
          
        </SettingLayout>
      ) : auth?.auth ? (
        <Unauthorized />
      ) : (
        <Navigate to="/sign-in" state={{ from: location }} replace />
      )}
    </>
  );
};

export default AuthLayout;
