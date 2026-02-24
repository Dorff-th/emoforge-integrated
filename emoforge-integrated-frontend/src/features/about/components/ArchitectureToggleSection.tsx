import { useState } from "react";
import { motion, AnimatePresence } from "framer-motion";

type Mode = "msa" | "mono";

export default function ArchitectureToggleSection() {
  const [mode, setMode] = useState<Mode>("msa");

  const isMsa = mode === "msa";

  const metrics = isMsa
    ? {
        containers: 11,
        memory: "699MB",
        swap: "1.4GB",
        deploy: "Manual Deploy",
      }
    : {
        containers: 4,
        memory: "531MB",
        swap: "160MB",
        deploy: "GitHub Actions",
      };

  return (
    <section className="space-y-16">
      {/* Toggle */}
      <div className="flex justify-center">
        <div className="flex rounded-xl bg-muted p-1">
          <button
            onClick={() => setMode("msa")}
            className={`px-6 py-2 rounded-lg text-sm font-semibold transition ${
              isMsa ? "bg-red-500 text-white shadow" : "text-muted-foreground"
            }`}
          >
            MSA (AS-IS)
          </button>

          <button
            onClick={() => setMode("mono")}
            className={`px-6 py-2 rounded-lg text-sm font-semibold transition ${
              !isMsa
                ? "bg-emerald-500 text-white shadow"
                : "text-muted-foreground"
            }`}
          >
            Monolith (TO-BE)
          </button>
        </div>
      </div>

      {/* Diagram */}
      <AnimatePresence mode="wait">
        <motion.div
          key={mode}
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          exit={{ opacity: 0, y: -20 }}
          transition={{ duration: 0.3 }}
          className="space-y-10"
        >
          {/* Frontend Layer */}
          <Layer title="Frontend Layer">
            {isMsa ? (
              <>
                <Box
                  title="Auth Frontend"
                  desc="1 container (auth-frontend)"
                  red
                />
                <Box
                  title="Post Frontend"
                  desc="1 container (post-frontend)"
                  red
                />
                <Box
                  title="Diary Frontend"
                  desc="1 container (diary-frontend)"
                  red
                />
                <Box
                  title="Admin Frontend"
                  desc="1 container (admin-frontend)"
                  red
                />
              </>
            ) : (
              <Box
                title="React SPA"
                desc="1 container (emoforge-frontend)"
                green
              />
            )}
          </Layer>

          {/* Backend Layer */}
          <Layer title="Backend Layer">
            {isMsa ? (
              <>
                <Box
                  title="Auth Service"
                  desc="1 container (auth-service)"
                  red
                />
                <Box
                  title="Attach Service"
                  desc="1 container (attach-service)"
                  red
                />
                <Box
                  title="Post Service"
                  desc="1 container (post-service)"
                  red
                />
                <Box
                  title="Diary Service"
                  desc="1 container (diary-service)"
                  red
                />
              </>
            ) : (
              <Box
                title="Integrated Backend"
                desc="1 container (emoforge-backend)"
                green
              />
            )}
          </Layer>

          {/* LLM Layer */}
          <Layer title="LLM Layer">
            <Box
              title={isMsa ? "LangGraph Service" : "LLM Service"}
              desc={
                isMsa
                  ? "1 container (langgraph_service - FastAPI)"
                  : "1 container (emoforge-llm)"
              }
              highlight
              red={isMsa}
              green={!isMsa}
            />
          </Layer>

          {/* Infra Layer */}
          <Layer title="Infrastructure Layer">
            {isMsa ? (
              <>
                <Box title="Nginx" desc="1 container (nginx)" red />
                <Box title="Certbot" desc="1 container (certbot)" red />
              </>
            ) : (
              <Box title="Nginx" desc="1 container (emoforge-nginx)" green />
            )}
          </Layer>

          {/* External DB */}
          <div className="mt-6 border-t pt-8 flex justify-center">
            <div className="rounded-xl border-dashed border-2 border-blue-400 bg-blue-50 px-10 py-6 text-center">
              <div className="font-semibold text-sm">
                AWS RDS (Managed Database)
              </div>
              <div className="text-xs text-muted-foreground mt-1">
                External Infrastructure (Not a container)
              </div>
            </div>
          </div>
        </motion.div>
      </AnimatePresence>

      {/* Metrics */}
      <div className="grid md:grid-cols-4 gap-6 text-center">
        <Metric label="Containers" value={metrics.containers} />
        <Metric label="Memory Used" value={metrics.memory} />
        <Metric
          label="Swap"
          value={metrics.swap}
          highlight={isMsa ? "danger" : "success"}
        />
        <Metric label="Deploy" value={metrics.deploy} />
      </div>
    </section>
  );
}

/* ========================= */

function Layer({
  title,
  children,
}: {
  title: string;
  children: React.ReactNode;
}) {
  return (
    <div>
      <div className="text-xs uppercase text-muted-foreground mb-3 tracking-wider">
        {title}
      </div>

      <div className="grid md:grid-cols-4 gap-4">{children}</div>
    </div>
  );
}

/* ========================= */

function Box({
  title,
  desc,
  red,
  green,
  highlight,
}: {
  title: string;
  desc?: string;
  red?: boolean;
  green?: boolean;
  highlight?: boolean;
}) {
  return (
    <div className="group relative">
      <div
        className={`
          rounded-xl border p-5 text-center transition
          ${red ? "border-red-400 bg-red-50" : ""}
          ${green ? "border-emerald-400 bg-emerald-50" : ""}
          ${highlight ? "ring-2 ring-offset-2" : ""}
        `}
      >
        <div className="font-semibold text-sm">{title}</div>
      </div>

      {desc && (
        <div
          className="absolute bottom-full left-1/2 -translate-x-1/2 mb-2
                        hidden group-hover:block
                        bg-black text-white text-xs px-3 py-1 rounded whitespace-nowrap"
        >
          {desc}
        </div>
      )}
    </div>
  );
}

/* ========================= */

function Metric({
  label,
  value,
  highlight,
}: {
  label: string;
  value: string | number;
  highlight?: "danger" | "success";
}) {
  const highlightStyle =
    highlight === "danger"
      ? "text-red-500"
      : highlight === "success"
        ? "text-emerald-500"
        : "";

  return (
    <div className="rounded-xl border p-6 bg-background">
      <div className="text-xs text-muted-foreground mb-2">{label}</div>

      <motion.div
        key={value}
        initial={{ scale: 0.8, opacity: 0 }}
        animate={{ scale: 1, opacity: 1 }}
        transition={{ duration: 0.2 }}
        className={`text-xl font-bold ${highlightStyle}`}
      >
        {value}
      </motion.div>
    </div>
  );
}
