interface FeatureCardProps {
  title: string;
  description: string;
  image: string;
}

export default function FeatureCard({
  title,
  description,
  image,
}: FeatureCardProps) {
  return (
    <div className="rounded-xl border bg-background shadow-sm hover:shadow-md transition overflow-hidden">
      <img src={image} alt={title} className="w-full h-40 object-cover" />
      <div className="p-6">
        <h3 className="text-lg font-semibold mb-3">{title}</h3>
        <p className="text-sm text-muted-foreground leading-relaxed">
          {description}
        </p>
      </div>
    </div>
  );
}
