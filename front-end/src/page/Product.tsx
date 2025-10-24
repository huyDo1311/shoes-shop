import { FilterIcon } from "lucide-react";
import { Sheet, SheetContent, SheetTrigger } from "@/components/ui/sheet";
import { useSearchParams } from "react-router-dom";
import ProductList from "@/components/shared/ProductList";
import { ToggleGroup, ToggleGroupItem } from "@/components/ui/toggle-group";
import SearchForm from "@/components/shared/SearchForm";
import { useQuery } from "@tanstack/react-query";
import productApi from "@/apis/product.api";
import type { SearchType } from "@/types/filter.type";


const Product = () => {
  const [searchParams, setSearchParams] = useSearchParams();
  // search form submit
  // const onSubmit = (data: SearchType) => {
  //   // Xóa giá trị rỗng ra khỏi params
  //   const cleanedData = Object.fromEntries(
  //     Object.entries(data).filter(([_, v]) => v !== undefined && v !== "" && v !== null)
  //   );

  //   // Cập nhật URL search params (triggers react-query refetch)
  //   setSearchParams(cleanedData);
  // };

  // const onSubmit = (data: SearchType) => {
  //   console.log("Form values:", data);
  //   // Xóa giá trị rỗng
  //   const cleanedData = Object.fromEntries(
  //     Object.entries(data)
  //       .filter(([_, v]) => v !== undefined && v !== "" && v !== null)
  //       .map(([k, v]) => [
  //         k,
  //         Array.isArray(v) ? v.join(",") : String(v), // ép tất cả thành string
  //       ])
  //   );

  //   setSearchParams(cleanedData);
  // };

   const onSubmit = (data: SearchType) => {
    const params = new URLSearchParams();

    for (const [key, value] of Object.entries(data)) {
      if (value) {
        if (Array.isArray(value)) {
          value.forEach((v) => params.append(key, v));
        } else {
          params.append(key, value.toString());
        }
      }
    }

    setSearchParams(params);
  };

  const paramsValues: Record<string, any> = {};
  for (const [key, value] of searchParams.entries()) {
    if (paramsValues[key]) {
      paramsValues[key] = Array.isArray(paramsValues[key])
        ? [...paramsValues[key], value]
        : [paramsValues[key], value];
    } else if (key === "size") {
      paramsValues[key] = [value];
    } else {
      paramsValues[key] = value;
    }
  }

  const sortTypes = [
    { id: 1, label: "Newest", value: "Newest" },
    { id: 3, label: "Price Acs", value: "PriceASC" },
    { id: 4, label: "Price Decs", value: "PriceDESC" },
  ];

  const { data } = useQuery({
    queryKey: ['products', paramsValues],
    queryFn: async () => {
      const result = await productApi.getProducts(paramsValues)
      return result.data.data
    }
  })
  // console.log('paramsValues', paramsValues)
  // console.log(data)

  const handleSort = (value: string) => {
    if (value !== "") setSearchParams({ ...paramsValues, sort: value });
    else {
      const { sort, ...rest } = paramsValues;
      console.log(sort);

      setSearchParams(rest);
    }
  };

  const searchFormRender = () => (
    <SearchForm onSubmit={onSubmit} defaultValues={paramsValues} />
  );

  return (
    <section className="py-8">
      <div className="max-w-7xl w-full mx-auto">
        {!data &&
          <h2 className="text-3xl text-center text-slate-950 font-semibold dark:text-white">
            Your Search Result
          </h2>
        }
        <div className="grid grid-cols-10 gap-5 px-3 mt-12">
          <div className="col-span-3 hidden lg:block">
            {searchFormRender()}{" "}
          </div>
          <div className="col-span-10 lg:col-span-7">
            <div className="flex justify-end space-x-5 lg:hidden  items-center">
              <Sheet>
                <SheetTrigger>
                  <FilterIcon className="size-5 lg:hidden block text-slate-800" />
                </SheetTrigger>
                <SheetContent className=" lg:hidden block">
                  {searchFormRender()}
                </SheetContent>
              </Sheet>
            </div>

            <div className="flex mt-5  items-center w-full justify-end">
              <ToggleGroup
                onValueChange={handleSort}
                defaultValue={paramsValues.sort ?? null}
                className="flex gap-3"
                type="single"
              >
                {sortTypes.map((sortType) => (
                  <ToggleGroupItem
                    className="text-sm  text-slate-950 dark:text-white rounded-xl border border-sky-200
                      dark:data-[state=on]:bg-slate-400/50 data-[state=on]:bg-black/15 data-[state=on]:border-sky-400
                    "
                    id={sortType.id.toString()}
                    value={sortType.value}
                  >
                    {sortType.label}
                  </ToggleGroupItem>
                ))}
              </ToggleGroup>
            </div>

            {/*  */}
            <div className="mt-8">
              {/*  */}
              {/* <ProductList
                className="grid grid-cols-1 sm:grid-cols-3  gap-5"
                products={data?.pages.flatMap((page) => page.content) || []}
              /> */}

              <ProductList
                className="grid grid-cols-1 sm:grid-cols-3 gap-5"
                products={data?.products || []}
              />

              {/*    */}



            </div>
          </div>
        </div>
      </div>
    </section>
  );
};

export default Product;
