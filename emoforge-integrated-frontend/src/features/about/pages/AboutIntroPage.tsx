import HeroSection from "../components/IntroSection/HeroSection";
import FeatureSection from "../components/IntroSection/FeatureSection";
import BoardSection from "../components/IntroSection/BoardSection";
import ArchitectureSection from "../components/IntroSection/ArchitectureSection";
import ClosingSection from "../components/IntroSection/ClosingSection";

const AboutIntroPage = () => {
  return (
    <div className="bg-background text-foreground">
      <HeroSection />
      <FeatureSection />
      <BoardSection />
      <ArchitectureSection />
      <ClosingSection />
    </div>
  );
};

export default AboutIntroPage;
