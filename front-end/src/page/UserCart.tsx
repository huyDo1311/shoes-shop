
import ToggleQuantity from "@/components/shared/ToggleQuantity";
import { Button } from "@/components/ui/button";
import { Checkbox } from "@/components/ui/checkbox";
// import { CheckoutContext } from "@/context/CheckoutContext";
import { Trash2Icon, XIcon } from "lucide-react";
import { useContext, useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";
import { toast } from "sonner";
import { useAuth } from "@/hooks/useAuth";
import { useQuery } from "@tanstack/react-query";
import { orderAPI } from "@/apis/order.api";
import type { Cart, CartItem } from "@/types/order.type";
import DeleteService from "@/components/shared/DeleteService";

const UserCart = () => {
  const navigate = useNavigate();
  const { currentUser } = useAuth();
  const email = currentUser?.email;

  // 🔹 Lấy dữ liệu giỏ hàng
  const { data, isLoading, isError } = useQuery({
    queryKey: ["cart", email],
    queryFn: () => orderAPI.getCart({ email: email as string }),
    enabled: !!email,
  });

  const cart: Cart | undefined = data?.data.data;
  const items = cart?.items ?? [];

  // 🔹 Lấy context checkout
  // const checkoutContext = useContext(CheckoutContext);
  // if (!checkoutContext) {
  //   throw new Error("CheckoutContext must be used within CheckoutProvider");
  // }

  // const { updateProducts, setTotalCost } = checkoutContext;

  // 🔹 Tính tổng toàn bộ giỏ hàng
  const total = useMemo(() => {
    return items.reduce(
      (acc: number, cart: CartItem) => acc + cart.price * cart.quantity,
      0
    );
  }, [items]);

  // 🔹 Khi thay đổi số lượng
  const handleQuantityChange = (cartId: number, newQuantity: number) => {
    // Cập nhật tạm thời trong items nếu cần
    // (ở đây không cần lưu local state, vì query sẽ re-fetch khi update)
  };

  // 🔹 Khi xóa item trong giỏ hàng
  const handleDelete = (cartId: number) => {
    toast.success(`Removed item #${cartId} from cart`);
  };

  // 🔹 Khi checkout
  const handleCheckout = () => {
    if (items.length === 0) {
      toast.info("Your cart is empty");
      return;
    }

    // updateProducts(items); // ✅ checkout toàn bộ sản phẩm trong giỏ
    // setTotalCost(total);
    navigate("/checkout");
  };

  // 🔹 Loading / Error / Empty state
  if (isLoading) return <p>Loading...</p>;
  if (isError) return <p>An error occurred</p>;
  if (items.length === 0)
    return (
      <div className="container flex justify-center h-screen">
        <h2 className="text-4xl mt-10 text-center text-indigo-400">
          Your cart is empty
        </h2>
      </div>
    );

  return (
    <section className="p-8 container">
      <div className="max-w-6xl w-full mx-auto">
        <h2 className="text-transparent mb-8 text-3xl text-center uppercase bg-clip-text bg-gradient-to-b py-2 from-slate-400 to-indigo-600 ">
          Your Shopping Cart
        </h2>

        {/* ❌ Bỏ Select All checkbox */}

        <div className="mt-5 grid grid-cols-2 md:grid-cols-4 max-w-4xl gap-4 w-full overflow-auto ">
          {items.map((cart: CartItem) => (
            <div
              key={cart.id}
              className="space-y-6 py-3 px-2 border rounded-lg shadow-sm"
            >
              <div className="relative">
                <img
                  src={cart.imageUrl as string}
                  alt={cart.productName}
                  className="aspect-square object-cover rounded-md w-full"
                />
              </div>

              <div className="space-y-1">
                <p className="text-base text-slate-600 dark:text-white truncate">
                  {cart.productName}
                </p>
                <p className="text-muted-foreground text-sm">
                  {cart.sizeValue} - {cart.colorName}
                </p>
                <p className="text-base text-red-500 py-1 text-center font-semibold">
                  ${cart.price * cart.quantity}
                </p>

                <div className="flex items-center justify-center">
                  <ToggleQuantity
                    quantity={cart.quantity}
                    cartId={cart.id}
                    sku={cart.sku}
                    onQuantityChange={(v) => handleQuantityChange(cart.id, v)}
                    max={cart.variantStock}
                  />
                </div>

                <div className="flex mt-3 justify-center items-center">
                  <DeleteService
                    api={orderAPI.deleteItemCart}
                    body={{
                      sku: cart.sku,
                      email: email
                    }}
                    invalidates={[["cart", email]]}
                    successMessage="Item removed"
                  >
                    <div className="gap-x-1 max-w-[100px] items-center cursor-pointer flex">
                      <XIcon className="size-4 text-gray-400" />
                      <span className="text-gray-400 text-sm">Remove</span>
                    </div>
                  </DeleteService>
                </div>
              </div>
            </div>
          ))}
        </div>

        <div className="px-8 flex justify-end w-full py-8">
          <div className="space-y-3 text-right">
            <p>
              Total: <span className="font-semibold">${total}</span>
            </p>
            <button
              onClick={handleCheckout}
              className="px-8 py-2 bg-black text-white text-sm rounded-md font-semibold hover:bg-black/80 hover:shadow-lg"
            >
              Checkout
            </button>
          </div>
        </div>
      </div>
    </section>
  );
};

export default UserCart;
