import {
  Sheet,
  SheetContent,
  SheetDescription,
  SheetFooter,
  SheetHeader,
  SheetTitle,
  SheetTrigger,
} from "@/components/ui/sheet";
import { Button } from "../ui/button";
import { ShoppingCartIcon, XIcon } from "lucide-react";


import { Link } from "react-router-dom";
import { useAuth } from "@/hooks/useAuth";
import { useMutation, useQuery } from "@tanstack/react-query";
import { orderAPI } from "@/apis/order.api";
import { useEffect } from "react";
import type { Cart } from "@/types/order.type";
import DeleteService from "@/components/shared/DeleteService";


const ToggleCart = () => {
 
  const {currentUser} = useAuth();
  const email = currentUser?.email;

    const {
    data,
    isPending,
    isError,
  } = useQuery({
    queryKey: ["cart", email], // 👈 key gắn với email
    queryFn: () => orderAPI.getCart({ email: email as string }),
    enabled: !!email, // chỉ fetch khi có email
  });

  const cart: Cart | undefined = data?.data.data;
  const items = cart?.items ?? [];

  return  (
    <Sheet>
      <SheetTrigger asChild>
        <Button size="sm" variant="outline">
          <ShoppingCartIcon className="size-4" />
        </Button>
      </SheetTrigger>
      <SheetContent className="!max-w-md">
        <SheetHeader>
          <SheetTitle className="text-cyan-500 font-normal">
            Your Shopping Cart ({items?.length ?? 0})
          </SheetTitle>
          <SheetDescription>
            Go to your cart to see the items you saved
          </SheetDescription>
        </SheetHeader>

        <div className="flex divide-y space-y-5 mt-8 flex-col overflow-y-auto max-h-[500px]">
          {items?.length === 0 && (
            <div className="flex flex-col items-center justify-center gap-y-3">
              <ShoppingCartIcon className="size-16 text-gray-300" />
              <p className="text-lg text-gray-300">Your cart is empty</p>
            </div>
          )}
          {isPending ? (
            <>
              <div className="flex flex-col items-center justify-center gap-y-3">
                <ShoppingCartIcon className="size-16 text-gray-300" />
                <p className="text-lg text-gray-300">Loading...</p>
              </div>
            </>
          ) : isError ? (
            <>
              <div className="flex flex-col items-center justify-center gap-y-3">
                <ShoppingCartIcon className="size-16 text-gray-300" />
                <p className="text-lg text-gray-300">An error occurred</p>
              </div>
            </>
          ) : (
            items?.map((item) => (
              <div
                className="py-2 flex xl:flex-row flex-col gap-y-2 justify-between items-center xl:items-start"
                key={item.id}
              >
                <div className=" flex items-center gap-x-3">
                  <img
                    src=''
                    alt={item.productName}
                    loading="lazy"
                    className="size-28 aspect-square object-cover rounded-md"
                  />
                  <div className="flex flex-col gap-y-3 ite">
                    <div className="space-y-1">
                      <p className="text-base font-semibold max-w-[200px] truncate">
                        {item.productName}
                      </p>
                      <p className="text-sm text-muted-foreground">
                        {item.sizeValue} - {item.colorName}
                      </p>
                      <p className="text-sm">
                        Quantity :{" "}
                        <span className="text-sky-500">{item.quantity}</span>
                      </p>
                    </div>

                    <DeleteService
                      sku={`${item.sku}`}
                      email={`${email}`}
                    >
                      <div className="gap-x-1 max-w-[100px] items-center cursor-pointer flex">
                        <XIcon className="size-4 text-gray-400" />
                        <span className="text-gray-400 text-sm">Remove</span>
                      </div>
                    </DeleteService>
                  </div>
                </div>
                <p className="text-base text-red-500 font-semibold font-inter">
                  {item.lineTotal}$
                </p>
              </div>
            ))
          )}
        </div>

        {items?.length > 0 && (
          <SheetFooter className="mt-5">
            <Link to={"/cart"}>
              <Button
                size="sm"
                className="block w-full px-6 py-2 bg-black dark:bg-slate-700 text-white rounded-lg font-bold transform hover:-translate-y-1 transition duration-400 "
              >
                Go to Cart
              </Button>
            </Link>
          </SheetFooter>
        )}
      </SheetContent>
    </Sheet>
  );
};

export default ToggleCart;
