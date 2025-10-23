import { Link } from "react-router-dom";
import ToggleTheme from "./ToggleTheme";
import { Sheet, SheetContent, SheetTrigger } from "../ui/sheet";
import { Menu, SettingsIcon, SquareChartGantt } from "lucide-react";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuGroup,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuShortcut,
  DropdownMenuTrigger,
} from "../ui/dropdown-menu";
import { Avatar, AvatarFallback, AvatarImage } from "../ui/avatar";
import { IoIosLogOut } from "react-icons/io";
import useLogout from "@/hooks/useSignOut";
import ToggleCart from "./ToggleCart";
import { CommandMenu } from "./CommandMenu";
import { useContext } from "react";
import AuthContext from "@/context/AuthContext";
import { useAuth } from "@/hooks/useAuth";

const Header = () => {
  const { currentUser } = useAuth();
  const logout = useLogout();

  const handleLogout = async () => {
    await logout();
  };

  const isUser = currentUser?.role?.includes("USER");

  const pageNavigation = (
    <>
      <h2 className="text-slate-950 md:hidden dark:text-neutral-300 font-bold">
        Spring Commerce
      </h2>
      <Link
        to="/"
        className="text-base text-muted-foreground font-light hover:text-gray-900 dark:hover:text-gray-100"
      >
        Home
      </Link>
      <p className="text-base text-muted-foreground font-light hover:text-gray-900 dark:hover:text-gray-100">
        About
      </p>
      <p className="text-base text-muted-foreground font-light hover:text-gray-900 dark:hover:text-gray-100">
        Contact
      </p>
      <Link
        to="/product"
        className="text-base text-muted-foreground font-light hover:text-gray-900 dark:hover:text-gray-100"
      >
        Products
      </Link>
    </>
  );

  return (
    <header className="w-full z-[50] bg-white dark:bg-[#09111f] sticky top-0 shadow-sm py-3">
      <div className="w-full px-5 flex justify-between items-center">
        {/* Logo */}
        <Link to="/" className="text-base font-bold hidden lg:block">
          Shoe shop
        </Link>

        {/* Desktop navigation */}
        <div className="md:flex gap-x-5 items-center hidden">
          {pageNavigation}
        </div>

        {/* Right section */}
        <div className="flex w-full lg:w-fit items-center gap-x-3">
          {/* Sheet for mobile menu */}
          <Sheet>
            <SheetTrigger>
              <Menu className="size-6 md:hidden text-muted-foreground" />
            </SheetTrigger>
            <SheetContent side="left">
              <div className="flex flex-col gap-y-5">{pageNavigation}</div>
            </SheetContent>
          </Sheet>

          {/* Theme toggle */}
          <ToggleTheme />

          {/* Command search */}
          <CommandMenu />

          {/* Authenticated user menu */}
          {currentUser ? (
            <>
              {/* Cart */}
              {isUser && <ToggleCart />}

              {/* Profile dropdown */}
              <DropdownMenu>
                <DropdownMenuTrigger>
                  <Avatar>
                    <AvatarImage src={currentUser?.photoUrl || "/user.png"} />
                    <AvatarFallback>
                      {currentUser.email.charAt(0).toUpperCase()}
                    </AvatarFallback>
                  </Avatar>
                </DropdownMenuTrigger>

                <DropdownMenuContent className="w-[250px]">
                  <DropdownMenuLabel>
                    {currentUser.email.charAt(0).toUpperCase()}
                  </DropdownMenuLabel>
                  <DropdownMenuGroup>
                    <Link to={isUser ? "/user" : "/admin"}>
                      <DropdownMenuItem className="text-md cursor-pointer">
                        {isUser ? "Profile" : "Settings"}
                        <DropdownMenuShortcut>
                          <SettingsIcon size={20} />
                        </DropdownMenuShortcut>
                      </DropdownMenuItem>
                    </Link>
                  </DropdownMenuGroup>

                  {isUser && (
                    <Link to="/orders">
                      <DropdownMenuItem className="lg:text-[15px] cursor-pointer">
                        Orders
                        <DropdownMenuShortcut>
                          <SquareChartGantt size={20} />
                        </DropdownMenuShortcut>
                      </DropdownMenuItem>
                    </Link>
                  )}

                  <DropdownMenuSeparator />
                  <DropdownMenuItem
                    onClick={handleLogout}
                    className="lg:text-[15px] cursor-pointer"
                  >
                    Sign Out
                    <DropdownMenuShortcut>
                      <IoIosLogOut size={20} />
                    </DropdownMenuShortcut>
                  </DropdownMenuItem>
                </DropdownMenuContent>
              </DropdownMenu>
            </>
          ) : (
            <Link
              to="/sign-in"
              className="text-gray-900 font-normal dark:text-white"
            >
              Sign In
            </Link>
          )}
        </div>
      </div>
    </header>
  );
};

export default Header;
