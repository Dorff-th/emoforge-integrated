// header.constants.ts
export const HEADER_MENU_ITEM_BASE =
  "flex items-center gap-1.5 px-3 py-2 text-sm font-medium transition-colors";  // HEADER_MENU_ITEM_BASE 

export const HEADER_ICON_SIZE = 16;
export const HEADER_ICON_SIZE_MOBILE = 14;

export const HEADER_HOVER_TRANSITION =
  "transition-colors duration-150 ease-out";

export const HEADER_HOVER_BG =
  "hover:bg-muted/70 rounded-md";

export const HEADER_HOVER_TEXT =
  "hover:text-foreground";


  export const HEADER_UNDERLINE =
  `
    after:absolute
    after:left-3
    after:right-3
    after:bottom-1
    after:h-[1px]
    after:bg-current
    after:transition-all
    after:duration-150
    after:ease-out
  `;

export const HEADER_HEIGHT = 64; // px