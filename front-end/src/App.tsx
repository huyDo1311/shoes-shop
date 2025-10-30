import { useState, useContext } from 'react';
import { createBrowserRouter, RouterProvider, Navigate, Outlet } from 'react-router-dom';
import LayoutDefault from '@/layout/LayoutDefault'
import Home from '@/page/Home'
import SinnIn from '@/page/SignIn'
import SignIn from '@/page/SignIn'
import SignUp from '@/page/SignUp'
import AuthLayout from '@/layout/AuthLayout';
import { adminNavbar, userNavbar } from "./data";
import Product from '@/page/Product';
import ProductDetails from '@/page/ProductDetails';
// import CartLayout from '@/layout/CartLayout';
import UserCart from '@/page/UserCart';
import Checkout from '@/page/Checkout';
import PaymentCallback from '@/page/PaymentCallback';
// import UserProfile from '@/page/UserProfile';


function App() {
  const router = createBrowserRouter([
    {
      path: "/",
      element: <LayoutDefault />,
      children: [
        {
          index: true,
          element: <Home />
        },
        {
          path: "/sign-in",
          element: <SignIn />,
        },
        {
          path: "/sign-up",
          element: <SignUp />,
        },
        {
          path: "/product",
          element: <Product />,
        },
        {
          path: "/product/:id",
          element: <ProductDetails />,
        },
        {
          path: "/cart",
          element: <UserCart />,
        },
        {
          path: "/checkout",
          element: <Checkout />,
        },
        {
          path: "/payment/payment-callback",
          element: <PaymentCallback />,
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
        //       path: "/payment/payment-callback",
        //       element: <PaymentCallback />,
        //     },
        //     {
        //       path: "/orders",
        //       // element: <UserOrders />,
        //     },
        //     {
        //       path: "/order/:id",
        //       // element: <OrderDetails />,
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
