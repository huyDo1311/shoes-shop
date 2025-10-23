import {useState, useContext} from 'react';
import {createBrowserRouter, RouterProvider, Navigate, Outlet} from 'react-router-dom';
import LayoutDefault from '@/layout/LayoutDefault'
import Home from '@/page/Home'
import SinnIn from '@/page/SignIn'
import SignIn from '@/page/SignIn'
import SignUp from '@/page/SignUp'
import AuthLayout from '@/layout/AuthLayout';
import { adminNavbar, userNavbar } from "./data";
import Product from '@/page/Product';

function App() {
  const router = createBrowserRouter([
    {
      path: "/",
      element: <LayoutDefault/>,
      children: [
        {
          index: true,
          element: <Home/>
        },
        {
          path: "/sign-in",
          element: <SignIn/>,
        },
        {
          path: "/sign-up",
          element: <SignUp />,
        },
        {
          path: "/product",
          element: <Product />,
        },
        // {
        //   element: <CartLayout />,
        //   children: [
        //     {
        //       path: "cart",
        //       element: <UserCart />,
        //     },
        //     {
        //       path: "/checkout",
        //       element: <Checkout />,
        //     },
        //     {
        //       path: "/orders",
        //       element: <UserOrders />,
        //     },
        //     {
        //       path: "/order/:id",
        //       element: <OrderDetails />,
        //     },
        //   ],
        // },
         {
          path: "/user",
          element: (
            <AuthLayout allowedRole={["ROLE_USER"]} navbar={userNavbar} />
          ),
          children: [
            {
              index: true,
              // element: <UserProfile />,
            },
            {
              path: "address",
              // element: <UserAddresses />,
            },
            {
              path: "change-password",
              // element: <ChangePassword />,
            },
            {
              path: "update-logo",
              // element: <UpdateLogo />,
            },
          ],
        },
        {
          path: "/admin",
          element: (
            <AuthLayout allowedRole={["ROLE_ADMIN"]} navbar={adminNavbar} />
          ),
          children: [
            {
              index: true,
              // element: <MangeCategories />,
            },
            {
              path: "manage-users",
              // element: <ManageUser />,
            },
            {
              path: "brand",
              // element: <ManageBrand />,
            },
            {
              path: "create-product",
              // element: <CreateProduct />,
            },
            {
              path: "change-password",
              // element: <ChangePassword />,
            },
            {
              path: "product-config",
              // element: <ManageConfig />,
            },
            {
              path: "manage-orders",
              // element: <ManageOrder />,
            },
            {
              path: "update-logo",
              // element: <UpdateLogo />,
            },
            {
              path: "manage-product",
              // element: <ManageProduct />,
            },
            {
              path: "update-product/:id",
              // element: <UpdateProduct />,
            },
            {
              path: "product/:id/config",
              // element: <ProductConfig />,
            },
          ],
        },
      ]

    }
  ])
  return <RouterProvider router={router} />;
}

export default App
