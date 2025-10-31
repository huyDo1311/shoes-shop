import { Button } from "@/components/ui/button";
import { Separator } from "@/components/ui/separator";
import { TruckIcon } from "lucide-react";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { toast } from "sonner";
import { useAuth } from "@/hooks/useAuth";
import { useMutation, useQuery } from "@tanstack/react-query";
import { orderAPI } from "@/apis/order.api";
import type { Cart, CartItem } from "@/types/order.type";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog";
import UserForm from "@/components/shared/UserForm";
import userApi from "@/apis/user.api";
import { userSchema, type UserFormType } from "@/types/user.type";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { paymentAPI } from "@/apis/payment.api";

type UserInfo = {
  userName: string;
  phone: string;
  address: string;
  email: string
};

const Checkout = () => {
  const navigate = useNavigate();
  const [open, setOpen] = useState(false);
  const [paymentMethod, setPaymentMethod] = useState("cash");

  // const [userInfo, setUserInfo] = useState<UserInfo>({
  //   userName: '',
  //   phone: '',
  //   address: '',
  //   email: '',
  // });

  const form = useForm<UserFormType>({
    resolver: zodResolver(userSchema),
    defaultValues: {
      email: "",
      userName: "",
      phone: "",
      address: "",
    },
  });

  const { currentUser } = useAuth();
  const email = currentUser?.email;

  const { data: fetchedUserInfo } = useQuery({
    queryKey: ["userInfo", email],
    queryFn: () => userApi.getUserInfo({ email: email as string }),
    enabled: !!email,
  });


  // useEffect(() => {
  //   if (fetchedUserInfo?.data?.data) {
  //     const info = fetchedUserInfo.data.data;
  //     if (!info.userName || !info.phone || !info.address) {
  //       toast.warning("Please complete your delivery information!");

  //     }
  //     setUserInfo({
  //       userName: info.userName || "",
  //       phone: info.phone || "",
  //       address: info.address || "",
  //       email: info.email
  //     });
  //   }
  // }, [fetchedUserInfo]);

  useEffect(() => {
    if (fetchedUserInfo?.data?.data) {
      const info = fetchedUserInfo.data.data;
      form.reset({
        email: info.email || "",
        userName: info.userName || "",
        phone: info.phone || "",
        address: info.address || "",
      });

      if (!info.userName || !info.phone || !info.address) {
        toast.warning("Please complete your delivery information!");
      }
    }
  }, [fetchedUserInfo, form]);

  // const userUpdateInfoMutation = useMutation({
  //   mutationFn: (body: UserFormType) => userApi.updateUserInfo(body),
  //   onSuccess: (res) => {
  //     toast.success("Cập nhật thông tin giao hàng thành công!");
  //     // Cập nhật lại state sau khi backend lưu thành công
  //     if (res.data?.data) {
  //       setUserInfo({
  //         userName: res.data.data.userName || "",
  //         phone: res.data.data.phone || "",
  //         address: res.data.data.address || "",
  //         email: res.data.data.email,
  //       });
  //     }
  //   },
  //   onError: (error: any) => {
  //     toast.error(error?.response?.data?.message || "Cập nhật thất bại!");
  //   },
  // });

  const userUpdateInfoMutation = useMutation({
    mutationFn: (body: UserFormType) => userApi.updateUserInfo(body),
    onSuccess: (res) => {
      toast.success("Cập nhật thông tin giao hàng thành công!");
      const updated = res.data?.data;
      if (updated) {
        form.reset({
          email: updated.email,
          userName: updated.userName || "",
          phone: updated.phone || "",
          address: updated.address || "",
        });
      }
    },
    onError: (error: any) => {
      toast.error(error?.response?.data?.message || "Cập nhật thất bại!");
    },
  });



  const handleUpdateUser = (values: UserFormType) => {
    if (!email) {
      toast.error("Không tìm thấy thông tin người dùng!");
      return;
    }

    // Gửi request update thông tin
    userUpdateInfoMutation.mutate({
      ...values,
      email,
    });
  };

  const { data, isLoading, isError } = useQuery({
    queryKey: ["cart", email],
    queryFn: () => orderAPI.getCart({ email: email as string }),
    enabled: !!email,
  });

  const cart: Cart | undefined = data?.data.data;
  const items = cart?.items ?? [];

  // const handlePlaceOrder = () => {
  //   if (!userInfo.userName || !userInfo.phone || !userInfo.address) {
  //     toast.error("Please complete your delivery information!");
  //     return;
  //   }

  //   toast.success("Đặt hàng thành công!");
  // };

  const checkOutMutation = useMutation({
    mutationFn: (body: { email: string }) => orderAPI.checkOut(body),
  })

  const createPaymentMutation = useMutation({
    mutationFn: (body: { amount: number ; language: string, email: string }) => paymentAPI.createPaymentUrl(body),
  });
  const createMomoPaymentMutation = useMutation({
    mutationFn: (body: { amount: number; email: string }) => paymentAPI.createMomoPayment(body),
  });

  const handlePlaceOrder = () => {
    const values = form.getValues();
    if (!values.userName || !values.phone || !values.address) {
      toast.error("Please complete your delivery information!");
      return;
    }
    if (!email) {
      toast.error("Không tìm thấy email người dùng!");
      return;
    }

    // checkOutMutation.mutate(
    //   { email },
    //   {
    //     onSuccess: () => {
    //       toast.success("Đặt hàng thành công!");
    //       // navigate("/order-success"); // Tùy bạn muốn chuyển trang không
    //     },
    //     onError: () => {
    //       toast.error("Đặt hàng thất bại, vui lòng thử lại!");
    //     },
    //   }
    // );
    if (paymentMethod === "vnpay") {
      // Thanh toán qua VNPAY
      createPaymentMutation.mutate(
        { amount: total,language: 'vn', email: email},
        {
          onSuccess: (res) => {
            const paymentUrl = res.data?.data;
            if (paymentUrl) {
              window.location.href = paymentUrl; // Redirect sang trang thanh toán
            } else {
              toast.error("Không nhận được liên kết thanh toán!");
            }
          },
          onError: () => {
            toast.error("Tạo liên kết thanh toán thất bại!");
          },
        }
      );
      return;
    }

    if (paymentMethod === "momo"){
      createMomoPaymentMutation.mutate(
        { amount: total, email: email},
        {
          onSuccess: (res) => {
            const paymentUrl = res.data?.data;
            if (paymentUrl) {
              window.location.href = paymentUrl; // Redirect sang trang thanh toán
            } else {
              toast.error("Không nhận được liên kết thanh toán!");
            }
          },
          onError: () => {
            toast.error("Tạo liên kết thanh toán thất bại!");
          },
        }
      );
      return;
    }

    if (paymentMethod === "cash") {
      // Thanh toán khi nhận hàng
      checkOutMutation.mutate(
        { email },
        {
          onSuccess: () => {
            toast.success("Đặt hàng thành công!");
            // navigate("/order-success");
          },
          onError: () => {
            toast.error("Đặt hàng thất bại, vui lòng thử lại!");
          },
        }
      );
      return;
    }
  };

  const total = items.reduce((sum, item) => sum + item.price * item.quantity, 0);

  return (
    items && (
      <section className="container py-8">
        <h2 className="text-transparent mb-8 text-3xl text-center uppercase bg-clip-text bg-gradient-to-b py-2 from-slate-400 to-indigo-600">
          Checkout
        </h2>

        <div className="max-w-6xl space-y-8 w-full mx-auto">
          {/* Thông tin giao hàng */}
          <div className="space-y-3">
            <div className="flex items-center justify-between">
              <p className="text-lg text-slate-700 dark:text-white">
                Delivery Information
              </p>
            </div>
            <Separator />

            <div className="flex justify-between items-start py-3">
              <div className="flex flex-col space-y-2 text-sm">
                <div className="flex items-center gap-3">
                  <p className="font-medium text-base text-black dark:text-white">
                    {/* {userInfo?.userName} */}
                    {form.watch("userName") || "—"}
                  </p>
                  <Separator orientation="vertical" className="h-4 dark:bg-gray-50" />
                  <p className="text-muted-foreground">
                    {/* {userInfo?.phone} */}
                    {form.watch("phone") || "—"}
                  </p>
                </div>

                <p className="text-sm text-muted-foreground">
                  {/* {userInfo?.address} */}
                  {form.watch("address") || "—"}
                </p>
              </div>

              {/* Dialog chỉnh sửa */}
              <Dialog open={open} onOpenChange={setOpen}>
                <DialogTrigger asChild>
                  <Button size="sm">Update information</Button>
                </DialogTrigger>
                <DialogContent className="sm:max-w-[420px]">
                  <DialogHeader>
                    <DialogTitle>Chỉnh sửa thông tin giao hàng</DialogTitle>
                  </DialogHeader>

                  {/* <UserForm
                    defaultValues={userInfo}
                    isRegister={false}
                    onSubmit={(values) => {
                      handleUpdateUser(values);
                      setOpen(false);
                    }}
                    isPending={false}
                  /> */}
                  <UserForm
                    form={form}
                    isRegister={false}
                    onSubmit={(values) => {
                      handleUpdateUser(values);
                      setOpen(false);
                    }}
                    isPending={userUpdateInfoMutation.isPending}
                  />
                </DialogContent>
              </Dialog>
            </div>
          </div>

          {/* Danh sách sản phẩm */}
          <div className="space-y-3">
            <div className="flex items-center justify-between">
              <p className="text-lg text-slate-700 dark:text-white">
                Your Order Summary
              </p>
            </div>
            <Separator />
            <div className="flex flex-wrap gap-3 items-center w-full">
              {items.map((product: CartItem) => (
                <div
                  key={product.id}
                  className="flex-col flex w-fit border items-center overflow-hidden 
                    py-2 px-2 gap-y-2 justify-center rounded-lg"
                >
                  <img
                    src={product.imageUrl as string}
                    alt={product.productName}
                    className="aspect-square w-40 object-cover rounded-sm"
                  />
                  <p className="text-slate-700 dark:text-white max-w-[150px] truncate ">
                    {product.productName}
                  </p>
                  <p className="text-red-500">
                    {product.quantity} x {product.price}$
                  </p>
                  <div className="text-muted-foreground text-sm">
                    {product.sizeValue} - {product.colorName}
                  </div>
                </div>
              ))}
            </div>
          </div>

          <Separator />

          {/* Tổng tiền + nút đặt hàng */}
          <div className="flex bottom-5 border shadow-sm px-3 py-2 border-slate-100 z-10 bg-white/5 justify-end items-center">
            <div className="flex-col space-y-3 max-w-lg w-full ">
              <div className="flex w-full items-center justify-between">
                <p className="text-base">Payment Method</p>
                <Select
                  value={paymentMethod}
                  onValueChange={(v) => setPaymentMethod(v)}
                >
                  <SelectTrigger className="w-[180px] text-sm">
                    <SelectValue placeholder="Select Method" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="cash">Cash on Delivery</SelectItem>
                    <SelectItem value="transfer">Chuyển khoản</SelectItem>
                    <SelectItem value="vnpay">VNPAY</SelectItem>
                    <SelectItem value="momo">Momo</SelectItem>
                  </SelectContent>
                </Select>
              </div>
              <div className="flex w-full items-center justify-between">
                <p className="text-lg">Total</p>
                <p className="text-black-500">{total}</p>
              </div>
              <div className="flex w-full items-center justify-end">
                {/* <Button
                  onClick={handlePlaceOrder}
                  size="sm"
                  disabled={!userInfo.userName || !userInfo.phone || !userInfo.address}
                >
                  <TruckIcon className="size-4 mr-2" />
                  Place Order
                </Button> */}
                <Button
                  onClick={handlePlaceOrder}
                  size="sm"
                  disabled={
                    !form.watch("userName") ||
                    !form.watch("phone") ||
                    !form.watch("address")
                  }
                >
                  <TruckIcon className="size-4 mr-2" />
                  {createPaymentMutation.isPending || checkOutMutation.isPending
                    ? "Processing..."
                    : "Place Order"}
                  Place Order
                </Button>
              </div>
            </div>
          </div>
        </div>
      </section>
    )
  );
};

export default Checkout;
