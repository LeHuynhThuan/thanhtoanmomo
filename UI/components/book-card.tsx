"use client"

import Image from "next/image"
import { Star, ShoppingCart } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Badge } from "@/components/ui/badge"

export interface Book {
  id: number
  title: string
  author: string
  price: number
  originalPrice?: number
  rating: number
  reviews: number
  cover: string
  badge?: string
}

export function BookCard({ book }: { book: Book }) {
  return (
    <article className="group flex flex-col overflow-hidden rounded-2xl border border-border bg-card shadow-sm transition-all hover:-translate-y-1 hover:shadow-lg">
      {/* Cover */}
      <div className="relative aspect-[3/4] overflow-hidden bg-secondary">
        <Image
          src={book.cover}
          alt={`Cover of ${book.title} by ${book.author}`}
          fill
          className="object-cover transition-transform duration-300 group-hover:scale-105"
          sizes="(max-width: 640px) 100vw, (max-width: 1024px) 50vw, 25vw"
        />
        {book.badge && (
          <Badge className="absolute left-3 top-3 rounded-lg bg-accent text-accent-foreground">
            {book.badge}
          </Badge>
        )}
      </div>

      {/* Details */}
      <div className="flex flex-1 flex-col gap-2 p-4">
        <h3 className="line-clamp-1 text-sm font-semibold leading-snug text-foreground">
          {book.title}
        </h3>
        <p className="text-xs text-muted-foreground">{book.author}</p>

        {/* Rating */}
        <div className="flex items-center gap-1">
          {Array.from({ length: 5 }).map((_, i) => (
            <Star
              key={i}
              className={`size-3.5 ${
                i < Math.floor(book.rating)
                  ? "fill-accent text-accent"
                  : "fill-muted text-muted"
              }`}
            />
          ))}
          <span className="ml-1 text-xs text-muted-foreground">
            ({book.reviews})
          </span>
        </div>

        {/* Price + Add to cart */}
        <div className="mt-auto flex items-center justify-between pt-2">
          <div className="flex items-baseline gap-2">
            <span className="text-lg font-bold text-foreground">
              ${book.price.toFixed(2)}
            </span>
            {book.originalPrice && (
              <span className="text-xs text-muted-foreground line-through">
                ${book.originalPrice.toFixed(2)}
              </span>
            )}
          </div>
          <Button
            size="icon-sm"
            variant="outline"
            className="rounded-lg border-accent/30 text-accent hover:bg-accent hover:text-accent-foreground"
            aria-label={`Add ${book.title} to cart`}
          >
            <ShoppingCart className="size-4" />
          </Button>
        </div>
      </div>
    </article>
  )
}
