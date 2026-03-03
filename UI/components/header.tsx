"use client"

import { useState } from "react"
import Link from "next/link"
import { Search, ShoppingCart, User, Menu, X, BookOpen } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Badge } from "@/components/ui/badge"

const navLinks = [
  { label: "Home", href: "#" },
  { label: "Categories", href: "#categories" },
  { label: "Best Sellers", href: "#featured" },
  { label: "New Arrivals", href: "#products" },
  { label: "Contact", href: "#footer" },
]

export function Header() {
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false)
  const [cartCount] = useState(3)

  return (
    <header className="sticky top-0 z-50 border-b border-border bg-card/80 backdrop-blur-md">
      <div className="mx-auto flex max-w-7xl items-center gap-4 px-4 py-3 lg:px-8">
        {/* Logo */}
        <Link href="#" className="flex shrink-0 items-center gap-2">
          <BookOpen className="size-7 text-accent" />
          <span className="font-serif text-2xl tracking-tight text-foreground">
            Bookly
          </span>
        </Link>

        {/* Nav links - desktop */}
        <nav className="hidden items-center gap-1 lg:flex" aria-label="Main navigation">
          {navLinks.map((link) => (
            <Link
              key={link.label}
              href={link.href}
              className="rounded-lg px-3 py-2 text-sm font-medium text-muted-foreground transition-colors hover:bg-secondary hover:text-foreground"
            >
              {link.label}
            </Link>
          ))}
        </nav>

        {/* Search bar - centered */}
        <div className="mx-auto hidden max-w-md flex-1 md:block">
          <div className="relative">
            <Search className="pointer-events-none absolute left-3 top-1/2 size-4 -translate-y-1/2 text-muted-foreground" />
            <Input
              type="search"
              placeholder="Search books, authors, genres..."
              className="h-10 rounded-xl border-border bg-secondary pl-10 text-sm placeholder:text-muted-foreground focus-visible:ring-accent"
            />
          </div>
        </div>

        {/* Right actions */}
        <div className="flex shrink-0 items-center gap-2">
          {/* Cart */}
          <Button variant="ghost" size="icon" className="relative" aria-label={`Shopping cart with ${cartCount} items`}>
            <ShoppingCart className="size-5" />
            {cartCount > 0 && (
              <Badge className="absolute -right-1 -top-1 flex size-5 items-center justify-center rounded-full bg-accent p-0 text-[10px] font-bold text-accent-foreground">
                {cartCount}
              </Badge>
            )}
          </Button>

          {/* Login / Register */}
          <Button
            variant="default"
            size="sm"
            className="hidden gap-2 rounded-xl sm:inline-flex"
          >
            <User className="size-4" />
            <span>Sign In</span>
          </Button>

          {/* Mobile menu toggle */}
          <Button
            variant="ghost"
            size="icon"
            className="lg:hidden"
            onClick={() => setMobileMenuOpen(!mobileMenuOpen)}
            aria-label="Toggle menu"
            aria-expanded={mobileMenuOpen}
          >
            {mobileMenuOpen ? <X className="size-5" /> : <Menu className="size-5" />}
          </Button>
        </div>
      </div>

      {/* Mobile search */}
      <div className="border-t border-border px-4 py-2 md:hidden">
        <div className="relative">
          <Search className="pointer-events-none absolute left-3 top-1/2 size-4 -translate-y-1/2 text-muted-foreground" />
          <Input
            type="search"
            placeholder="Search books..."
            className="h-9 rounded-xl border-border bg-secondary pl-10 text-sm"
          />
        </div>
      </div>

      {/* Mobile nav */}
      {mobileMenuOpen && (
        <nav className="border-t border-border bg-card px-4 py-4 lg:hidden" aria-label="Mobile navigation">
          <div className="flex flex-col gap-1">
            {navLinks.map((link) => (
              <Link
                key={link.label}
                href={link.href}
                className="rounded-lg px-3 py-2.5 text-sm font-medium text-muted-foreground transition-colors hover:bg-secondary hover:text-foreground"
                onClick={() => setMobileMenuOpen(false)}
              >
                {link.label}
              </Link>
            ))}
            <Button variant="default" size="sm" className="mt-2 gap-2 rounded-xl sm:hidden">
              <User className="size-4" />
              <span>Sign In</span>
            </Button>
          </div>
        </nav>
      )}
    </header>
  )
}
