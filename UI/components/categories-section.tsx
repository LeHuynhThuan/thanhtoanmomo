import { BookOpen, Briefcase, GraduationCap, Lightbulb, Heart, Globe } from "lucide-react"

const categories = [
  {
    name: "Fiction",
    description: "Novels, short stories & literary fiction",
    icon: BookOpen,
    count: "12,400+",
  },
  {
    name: "Business",
    description: "Strategy, leadership & entrepreneurship",
    icon: Briefcase,
    count: "8,200+",
  },
  {
    name: "Education",
    description: "Textbooks, study guides & reference",
    icon: GraduationCap,
    count: "6,800+",
  },
  {
    name: "Self-Help",
    description: "Personal growth & motivation",
    icon: Lightbulb,
    count: "5,100+",
  },
  {
    name: "Romance",
    description: "Love stories & contemporary romance",
    icon: Heart,
    count: "9,300+",
  },
  {
    name: "Travel",
    description: "Guides, memoirs & adventure",
    icon: Globe,
    count: "3,700+",
  },
]

export function CategoriesSection() {
  return (
    <section id="categories" className="bg-background py-16 md:py-24">
      <div className="mx-auto max-w-7xl px-4 lg:px-8">
        {/* Section header */}
        <div className="mb-12 text-center">
          <span className="mb-2 inline-block text-sm font-semibold uppercase tracking-wider text-accent">
            Browse by Genre
          </span>
          <h2 className="text-balance font-serif text-3xl tracking-tight text-foreground md:text-4xl">
            Explore Our Categories
          </h2>
          <p className="mt-3 text-pretty text-muted-foreground">
            Find your perfect read across our curated collection of genres.
          </p>
        </div>

        {/* Category grid */}
        <div className="grid grid-cols-2 gap-4 md:grid-cols-3 lg:grid-cols-6">
          {categories.map((category) => {
            const Icon = category.icon
            return (
              <button
                key={category.name}
                className="group flex flex-col items-center gap-3 rounded-2xl border border-border bg-card p-6 text-center shadow-sm transition-all hover:-translate-y-1 hover:border-accent/40 hover:shadow-md"
              >
                <div className="flex size-14 items-center justify-center rounded-xl bg-secondary text-accent transition-colors group-hover:bg-accent group-hover:text-accent-foreground">
                  <Icon className="size-6" />
                </div>
                <div>
                  <h3 className="text-sm font-semibold text-foreground">
                    {category.name}
                  </h3>
                  <p className="mt-1 hidden text-xs text-muted-foreground lg:block">
                    {category.description}
                  </p>
                  <span className="mt-1 block text-xs font-medium text-accent">
                    {category.count}
                  </span>
                </div>
              </button>
            )
          })}
        </div>
      </div>
    </section>
  )
}
