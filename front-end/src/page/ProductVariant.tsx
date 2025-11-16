import CreateProductItem from "@/components/shared/CreateProductVariant";
import { DataTableColumnHeader } from "@/components/ui/ColumnHeader";
import { DataTable } from "@/components/ui/DataTable";
import { type ColumnDef } from "@tanstack/react-table";
import { Trash2Icon } from "lucide-react";
import { useParams } from "react-router-dom";
import productApi from '@/apis/product.api';
import { useQuery } from '@tanstack/react-query';
import { type Variant } from '@/types/variant.type';
import EditProductVariant from "@/components/shared/EditProductVariant";
import DeleteService from '@/components/shared/DeleteService';
import variantApi from "@/apis/variant.api";

const ProductVariant = () => {

  const { id } = useParams<{ id: string }>();

  const { data, isLoading, isError, isSuccess } = useQuery({
    queryKey: ["product-detail", id],
    queryFn: () => productApi.getProductDetail(id!).then((res) => res.data.data.variant),
    enabled: !!id,
  });

  // console.log('data', data)

  if (isLoading) return <div>Loading...</div>;
  if (isError) return <div>Error...</div>;
  const columns: ColumnDef<Variant>[] = [
    {
      accessorKey: "id",
      header: "ID",
    },
    {
      accessorKey: "quantity",
      header: ({ column }) => (
        <DataTableColumnHeader column={column} title="Quantity" />
      ),
    },
    {
      accessorKey: "color",
      header: ({ column }) => (
        <DataTableColumnHeader column={column} title="Color" />
      ),
    },
    {
      accessorKey: "size",
      header: ({ column }) => (
        <DataTableColumnHeader column={column} title="Size" />
      ),
    },
    {
      id: "actions",
      header: "Actions",
      cell: ({ row }) => (
        <div className="flex items-center gap-x-2 ">
          <EditProductVariant
            productId={parseInt(id!)}
            variant={row.original}
          />
          <DeleteService
            api={() => variantApi.deleteVariant(row.original.sku)}
            invalidates={[["product-detail", id]]}
            successMessage="Variant deleted successfully!"
          >
            <Trash2Icon className="size-5 text-red-500" />
          </DeleteService>
        </div>
      ),
    },
  ];

  return (
    <section className="py-8 ">
      <div className="max-w-6xl mx-auto flex flex-col gap-y-5 px-4 w-full ">
        <h2 className="mb-8  uppercase text-2xl text-center text-sky-700">
          Product Variant for ID: <span className="text-sky-700">{id}</span>
        </h2>
        <div className="w-full ml-auto">
          <CreateProductItem productId={id!} />
        </div>
        <DataTable columns={columns} data={data} />
      </div>
    </section>
  );
};

export default ProductVariant;
