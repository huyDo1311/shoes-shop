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
import axios from "axios";

export type DeleteServiceProps = {
  api?: (body: any) => Promise<any>;
  // endpoint?: string;
  method?: "post" | "delete" | "put";
  body?: any;
  invalidates?: any[];
  successMessage?: string;
  children: React.ReactNode;
};

const DeleteService = ({
  api,
  // endpoint,
  method = "post",
  body,
  invalidates = [],
  successMessage = "Deleted successfully",
  children,
}: DeleteServiceProps) => {
  const [open, setOpen] = useState(false);
  const queryClient = useQueryClient();

  const mutation = useMutation({
    mutationFn: async () => {
      if (api) return api(body);

      // return axios({
      //   url: endpoint!,
      //   method,
      //   data: body,
      // });
    },
    onSuccess: () => {
      toast.success(successMessage);
      invalidates.forEach((key) =>
        queryClient.invalidateQueries({ queryKey: key })
      );
      setOpen(false);
    },
    onError: () => toast.error("Error deleting"),
  });

  return (
    <AlertDialog open={open} onOpenChange={setOpen}>
      <AlertDialogTrigger asChild>{children}</AlertDialogTrigger>

      <AlertDialogContent>
        <AlertDialogHeader>
          <AlertDialogTitle>Are you absolutely sure?</AlertDialogTitle>
          <AlertDialogDescription>
            This action cannot be undone.
          </AlertDialogDescription>
        </AlertDialogHeader>

        <AlertDialogFooter>
          <AlertDialogCancel>Cancel</AlertDialogCancel>
          <Button
            onClick={() => mutation.mutate()}
            className="bg-transparent text-red-500 border border-red-500"
          >
            Delete
          </Button>
        </AlertDialogFooter>
      </AlertDialogContent>
    </AlertDialog>
  );
};

export default DeleteService;
