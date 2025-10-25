import AddToCart from "@/components/shared/AddToCart";
import ProductPhotosAlbum from "@/components/shared/ProductPhotosAlbum";
import QuantityInput from "@/components/shared/QuantityInput";
import {
  Accordion,
  AccordionContent,
  AccordionItem,
  AccordionTrigger,
} from "@/components/ui/accordion";
import {
  Breadcrumb,
  BreadcrumbItem,
  BreadcrumbLink,
  BreadcrumbList,
  BreadcrumbPage,
  BreadcrumbSeparator,
} from "@/components/ui/breadcrumb";
import { ToggleGroup, ToggleGroupItem } from "@/components/ui/toggle-group";
import { productDetailsAdvertisement } from "@/data";
import { useState } from "react";
import { Link, useParams } from "react-router-dom";
import DOMPurify from "dompurify";
import productApi from "@/apis/product.api";
import { useQuery } from "@tanstack/react-query";

const ProductDetails = () => {
  const { id } = useParams<{ id: string }>();

  const [selectedSize, setSelectedSize] = useState<string | null>(null);
  const [selectedColor, setSelectedColor] = useState<string | null>(null);
  const [selectedQuantity, setSelectedQuantity] = useState(1);

  const { data, isLoading, isError, isSuccess } = useQuery({
    queryKey: ["product-detail", id],
    queryFn: () => productApi.getProductDetail(id!).then((res) => res.data.data),
    enabled: !!id,
  });

  if (isLoading) return <div>Loading...</div>;
  if (isError || !data) return <div>Product not found</div>;

  const product = data;

  // ✅ Lấy danh sách màu (unique)
  const colors = [...new Set(product.variant.map((v: any) => v.color))];

  // ✅ Lọc size theo màu được chọn
  const sizes = selectedColor
    ? [
      ...new Set(
        product.variant
          .filter((v: any) => v.color === selectedColor)
          .map((v: any) => v.size)
      ),
    ]
    : [];

  // ✅ Tìm tồn kho hiện tại khi chọn đủ màu và size
  const currentStock =
    selectedColor && selectedSize
      ? product.variant.find(
        (v: any) => v.color === selectedColor && v.size === selectedSize
      )?.quantity
      : 0;

  console.log(currentStock);

  return (
    isSuccess && (
      <section className="py-8">
        <div className="max-w-7xl px-3 mx-auto">
          {/* 🧭 Breadcrumb */}
          <div className="w-max mr-auto mb-8">
            <Breadcrumb>
              <BreadcrumbList>
                <BreadcrumbItem>
                  <Link to={"/"}>
                    <BreadcrumbLink>Home</BreadcrumbLink>
                  </Link>
                </BreadcrumbItem>
                <BreadcrumbSeparator />
                <BreadcrumbItem>
                  <BreadcrumbLink>{product.category.categoryName}</BreadcrumbLink>
                </BreadcrumbItem>
                <BreadcrumbSeparator />
                <BreadcrumbItem>
                  <BreadcrumbPage className="truncate max-w-28">
                    {product.productName}
                  </BreadcrumbPage>
                </BreadcrumbItem>
              </BreadcrumbList>
            </Breadcrumb>
          </div>

          <div className="flex sm:flex-nowrap flex-wrap gap-y-3 gap-x-10">
            {/* 🖼 Hình ảnh */}
            <div className="sm:w-1/2 w-full space-y-8">
              <img
                src={product.thumbnail}
                loading="lazy"
                alt={product.productName}
                className="w-full aspect-auto object-cover rounded-sm"
              />
              <ProductPhotosAlbum productImages={product.productImages ?? []} />
            </div>

            {/* 📋 Thông tin sản phẩm */}
            <div className="flex w-full sm:w-1/2 flex-col gap-y-6 items-center sm:items-start ">
              <h2 className="text-4xl font-bold text-slate-800 dark:text-gray-100">
                {product.productName}
              </h2>

              <p className="text-sky-600 text-2xl tracking-wide">
                {product.price}$
              </p>



              {/* 🎨 Chọn màu */}
              <div className="space-y-2">
                <p className="font-semibold text-base">Color:</p>
                <ToggleGroup
                  onValueChange={(value) => {
                    setSelectedColor(value);
                    setSelectedSize(null); // reset size khi đổi màu
                  }}
                  value={selectedColor || ""}
                  type="single"
                  variant="outline"
                >
                  {colors.map((color) => (
                    <ToggleGroupItem
                      key={color}
                      value={color}
                      className="border-sky-300 flex flex-col items-center !h-min p-2 data-[state=on]:bg-sky-100 dark:data-[state=on]:bg-slate-600"
                    >
                      <div
                        className="w-6 h-6 rounded-full border"
                        style={{ backgroundColor: color.toLowerCase() }}
                      ></div>
                      <span className="text-sm mt-1">{color}</span>
                    </ToggleGroupItem>
                  ))}
                </ToggleGroup>
              </div>

              {/* 📏 Chọn size */}
              
              <div className="space-y-2">
                <p className="font-semibold text-base">Size:</p>
                <ToggleGroup
                  onValueChange={(value) => setSelectedSize(value)}
                  value={selectedSize || ""}
                  type="single"
                  variant="outline"
                >
                  {sizes
                    .sort((a, b) => a - b)
                    .map((size) => (
                      <ToggleGroupItem
                        key={size}
                        value={size}
                        className="border-sky-300 font-normal text-base  
                            data-[state=on]:bg-sky-100 dark:data-[state=on]:bg-slate-600"
                      >
                        {size}
                      </ToggleGroupItem>
                    ))}
                </ToggleGroup>
              </div>


              {/* 🔢 Số lượng */}
              {/* <div>Số lượng: {currentStock}</div> */}
              <QuantityInput
                quantity={selectedQuantity}
                setQuantity={setSelectedQuantity}
                max={currentStock as number}
              />

              <p>
                Condition:{" "}
                <span className="text-sky-500">
                  {currentStock && currentStock > 0
                    ? `${currentStock} available`
                    : ""}
                </span>
              </p>

              {/* 🛒 Nút Add to Cart */}
              <AddToCart
                quantity={selectedQuantity}
                variant={
                  selectedColor && selectedSize
                    ? product.variant.find(
                      (v: any) =>
                        v.color === selectedColor && v.size === selectedSize
                    )?.sku
                    : null
                }
              />

              {/* 📣 Quảng cáo thêm */}
              <div className="flex flex-col divide-y mt-6">
                {productDetailsAdvertisement.map((item) => (
                  <div
                    className="flex gap-x-2 p-3 items-center"
                    key={item.title}
                  >
                    {item.icon}
                    <p className="text-base lg:text-xl text-gray-500 dark:text-gray-100">
                      {item.title}
                    </p>
                  </div>
                ))}
              </div>
            </div>
          </div>

          {/* 📄 Mô tả sản phẩm */}
          <div className="max-w-5xl mx-auto w-full my-5">
            <Accordion type="single" className="w-full" collapsible>
              <AccordionItem value="desc">
                <AccordionTrigger>
                  <h2 className="text-xl text-muted-foreground">Description</h2>
                </AccordionTrigger>
                <AccordionContent>
                  <div
                    dangerouslySetInnerHTML={{
                      __html: DOMPurify.sanitize(product.description),
                    }}
                  />
                </AccordionContent>
              </AccordionItem>
            </Accordion>
          </div>
        </div>
      </section>
    )
  );
};

export default ProductDetails;
