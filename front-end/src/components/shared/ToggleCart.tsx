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

const ToggleCart = () => {
 

  return (
    <Sheet>
      <SheetTrigger asChild>
        <Button size="sm" variant="outline">
          <ShoppingCartIcon className="size-4" />
        </Button>
      </SheetTrigger>
      <SheetContent className="!max-w-md">
        <SheetHeader>
          <SheetTitle className="text-cyan-500 font-normal">
            Your Shopping Cart 
          </SheetTitle>
          <SheetDescription>
            Go to your cart to see the items you saved
          </SheetDescription>
        </SheetHeader>

        <div className="flex divide-y space-y-5 mt-8 flex-col overflow-y-auto max-h-[500px]">
          
            <div className="flex flex-col items-center justify-center gap-y-3">
              <ShoppingCartIcon className="size-16 text-gray-300" />
              <p className="text-lg text-gray-300">Your cart is empty</p>
            </div>
          
         
            <>
              <div className="flex flex-col items-center justify-center gap-y-3">
                <ShoppingCartIcon className="size-16 text-gray-300" />
                <p className="text-lg text-gray-300">Loading...</p>
              </div>
            </>
        
            <>
              <div className="flex flex-col items-center justify-center gap-y-3">
                <ShoppingCartIcon className="size-16 text-gray-300" />
                <p className="text-lg text-gray-300">An error occurred</p>
              </div>
            </>
          
    
              <div
                className="py-2 flex xl:flex-row flex-col gap-y-2 justify-between items-center xl:items-start"
              >
                <div className=" flex items-center gap-x-3">
                  <img
                    loading="lazy"
                    className="size-28 aspect-square object-cover rounded-md"
                  />
                  <div className="flex flex-col gap-y-3 ite">
                    <div className="space-y-1">
                      <p className="text-base font-semibold max-w-[200px] truncate">
                        Name
                      </p>
                      <p className="text-sm text-muted-foreground">
                        Size - color
                      </p>
                      <p className="text-sm">
                        Quantity :{" "}
                        <span className="text-sky-500">3</span>
                      </p>
                    </div>

                      <div className="gap-x-1 max-w-[100px] items-center cursor-pointer flex">
                        <XIcon className="size-4 text-gray-400" />
                        <span className="text-gray-400 text-sm">Remove</span>
                      </div>
                    
                  </div>
                </div>
                <p className="text-base text-red-500 font-semibold font-inter">
                  Price$
                </p>
              </div>
        
        </div>

       
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
       
      </SheetContent>
    </Sheet>
  );
};

export default ToggleCart;
