import { useState } from "react";
import {
  AlertDialog,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
  AlertDialogTrigger,
} from "../ui/alert-dialog";
import { Button } from "../ui/button";
import { toast } from "sonner";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { orderAPI } from "@/apis/order.api";

export type DeleteItemCartProps = {
  sku?: string ,
  email?: string,
  children: React.ReactNode;
};

const DeleteService = ({sku, email, children } : DeleteItemCartProps) => {
  const [open, setOpen] = useState(false);
  const [loading, setLoading] = useState(false);

  const queryClient = useQueryClient();

    const deleteItemMutation = useMutation({
    mutationFn: (body: Omit<DeleteItemCartProps, "children">) =>
      orderAPI.deleteItemCart(body),
    onSuccess: (result) => {
      toast.success("Item removed from cart successfully!");
      queryClient.invalidateQueries({ queryKey: ["cart", email] });
      setOpen(false);
    },
    onError: () => {
      toast.error("Error deleting item from cart");
    },
  });

  const handleDelete = () => {
    if (!sku || !email) {
      toast.error("Missing SKU or user email");
      return;
    }
    deleteItemMutation.mutate({ sku, email });
  };

  return (
    <>
      <AlertDialog open={open} onOpenChange={setOpen}>
        <AlertDialogTrigger className="text-red-500 text-[16px] hover:underline">
          {children}
        </AlertDialogTrigger>
        <AlertDialogContent>
          <AlertDialogHeader>
            <AlertDialogTitle className="text-amber-600 ">
              Are you absolutely sure?
            </AlertDialogTitle>
            <AlertDialogDescription>
              This action cannot be undone.
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel>Cancel</AlertDialogCancel>
            <Button
              disabled={loading}
              onClick={handleDelete}
              className="bg-transparent text-red-500 border border-slate-500 hover:bg-transparent hover:border-red-500"
            >
              {loading ? "Deleting..." : "Delete"}
            </Button>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>
    </>
  );
};

export default DeleteService;
