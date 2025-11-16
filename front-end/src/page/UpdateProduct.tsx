import productApi from "@/apis/product.api";
import ProductForm from "@/components/shared/ProductForm";
import { Button } from "@/components/ui/button";
import { type BodyCreateProduct } from "@/types/product.type";
import { useQuery } from "@tanstack/react-query";
import React from "react";
import { useNavigate, useParams } from "react-router-dom";

const UpdateProduct = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();

  // Fetch product detail
  const { data, isLoading, isError, isSuccess } = useQuery({
    queryKey: ["productDetail", id],
    queryFn: () => productApi.getProductDetail(id!),
    enabled: !!id,
  });

  const [defaultValues, setDefaultValues] = React.useState<BodyCreateProduct | null>(null);
  const [images, setImages] = React.useState<string[]>([]);

  React.useEffect(() => {
    if (isSuccess && data?.data?.data) {
      const product = data.data.data;

      // Lấy ra danh sách URL từ mảng productImages
      const imageUrls = product.productImages?.map((img: any) => img.image_url) || [];

      setDefaultValues({
        productName: product.productName,
        price: product.price,
        description: product.description,
        categoryId: product.category.id.toString(),
        brandId: product.brand.id.toString(),
        thumbnail: product.thumbnail || "",
        images: imageUrls,
      });

      setImages(imageUrls);
    }
  }, [isSuccess, data]);

  if (isLoading) return <div>Loading...</div>;
  if (isError) return <div>Error...</div>;

  return (
    isSuccess &&
    defaultValues && (
      <section className="py-8">
        <div className="container max-w-6xl w-full">
          <h2 className="mb-8 uppercase text-2xl text-center text-sky-700">
            Update Product
          </h2>

          <div className="w-full py-8 mr-auto">
            <Button
              onClick={() => navigate(`/admin/product/${id}/variant`)}
              size="sm"
              variant="secondary"
            >
              Edit Variant
            </Button>
          </div>

          <div className="grid grid-cols-10 gap-2">
            {/* Form update */}
            <ProductForm
              images={images}
              setImages={setImages}
              className="space-y-8 col-span-10 lg:col-span-7"
              id={id}
              defaultValues={defaultValues}
              type="update"
            />

            {/* Hiển thị hình ảnh */}
            <div className="lg:col-span-3 col-span-10 w-full">
              <p className="text-center font-semibold">Images</p>
              <div className="flex flex-wrap items-center py-5 gap-5">
                {images.map((img, index) => (
                  <div key={index} className="relative">
                    <img
                      src={img}
                      alt="product"
                      className="aspect-square size-32 object-cover rounded-md border"
                    />
                    <div
                      onClick={() => setImages((prev) => prev.filter((_, i) => i !== index))}
                      className="absolute top-0 right-0 border rounded-full size-5 flex justify-center items-center border-slate-700 bg-black/50 brightness-125 cursor-pointer"
                    >
                      <span className="text-red-500 text-xs font-bold">X</span>
                    </div>
                  </div>
                ))}
              </div>

              <p className="text-center font-semibold mt-6">Thumbnail</p>
              <div className="flex justify-center relative mt-3">
                <img
                  src={defaultValues?.thumbnail || ""}
                  alt="thumbnail"
                  className="size-60 aspect-square object-cover rounded-md border"
                />
                {defaultValues?.thumbnail && (
                  <div
                    onClick={() =>
                      setDefaultValues((prev) =>
                        prev ? { ...prev, thumbnail: "" } : prev
                      )
                    }
                    className="absolute top-0 right-0 border rounded-full size-5 flex justify-center items-center border-slate-700 bg-black/50 brightness-125 cursor-pointer"
                  >
                    <span className="text-red-500 text-xs font-bold">X</span>
                  </div>
                )}
              </div>
            </div>
          </div>
        </div>
      </section>
    )
  );
};

export default UpdateProduct;
