import { type DialogProps } from "@radix-ui/react-dialog";

import { cn } from "@/lib/utils";
import { Button } from "@/components/ui/button";
import {
  CommandDialog,
  CommandEmpty,
  CommandGroup,
  CommandInput,
  CommandItem,
  CommandList,
  CommandSeparator,
} from "@/components/ui/command";
// import useFetchData from "@/hooks/useFetchData";
// import { useDebounce } from "@/hooks/useDebounce";
import { useState, useEffect } from "react";
import { PiSneakerThin } from "react-icons/pi";
import { useNavigate } from "react-router-dom";
import { useDebounce } from "@/hooks/useDebounce";
import productApi from "@/apis/product.api";
import { useQuery } from "@tanstack/react-query";
import type { Product, ProductListType } from "@/types/product.type";

export function CommandMenu({ ...props }: DialogProps) {
  const [open, setOpen] = useState(false);
  const [search, setSearch] = useState("");
  const debouncedSearch = useDebounce(search, 500);
  const navigate = useNavigate();

  const { data, isLoading, isError } = useQuery({
    queryKey: ["search-products", debouncedSearch],
    queryFn: async () => {
      if (!debouncedSearch.trim()) return [];
      const res = await productApi.searchProductName(debouncedSearch);
      return res.data ?? [];
    },
    enabled: !!debouncedSearch.trim(),
  });

  const products = data as Product[]

  useEffect(() => {
    const down = (e: KeyboardEvent) => {
      if ((e.key === "k" && (e.metaKey || e.ctrlKey)) || e.key === "/") {
        if (
          (e.target instanceof HTMLElement && e.target.isContentEditable) ||
          e.target instanceof HTMLInputElement ||
          e.target instanceof HTMLTextAreaElement ||
          e.target instanceof HTMLSelectElement
        ) {
          return;
        }

        e.preventDefault();
        setOpen((open) => !open);
      }
    };

    document.addEventListener("keydown", down);
    return () => document.removeEventListener("keydown", down);
  }, []);



  return (
    <>
      <Button
        variant="outline"
        onClick={() => setOpen(true)}
        className={cn(
          "relative h-8 w-full justify-start rounded-[0.5rem] bg-muted/50 text-sm font-normal text-muted-foreground shadow-none sm:pr-12 md:w-40 lg:w-64"
        )}
        {...props}
      >
        <span className="hidden lg:inline-flex">Search products ...</span>
        <span className="inline-flex lg:hidden">Type product name</span>
        <kbd className="pointer-events-none absolute right-[0.3rem] top-[0.3rem] hidden h-5 select-none items-center gap-1 rounded border bg-muted px-1.5 font-mono text-[10px] font-medium opacity-100 sm:flex">
          <span className="text-xs">⌘</span>K
        </kbd>
      </Button>

      <CommandDialog open={open} onOpenChange={setOpen}>
        <CommandInput
          placeholder="Type a product name..."
          value={search}
          onValueChange={setSearch}
        />
        <CommandList>
          {isLoading && <p className="p-2 text-sm text-muted-foreground">Loading...</p>}
          {isError && <p className="p-2 text-sm text-red-500">Error loading products</p>}
          {!isLoading && products?.length === 0 && <CommandEmpty>No results found.</CommandEmpty>}
          <CommandGroup heading="Products">
            {products?.map((product) => (
              <CommandItem
                key={product.id}
                value={product.productName}
                onSelect={() => {
                  setOpen(false);
                  setSearch("");
                  navigate(`/product/${product.id}`);
                }}
              >
                <PiSneakerThin className="mr-2 h-4 w-4" />
                {product.productName}
              </CommandItem>
            ))}

          </CommandGroup>

          <CommandSeparator />
        </CommandList>
      </CommandDialog>
    </>
  );
}
