/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ['./index.html', './src/**/*.{js,ts,jsx,tsx}'],
  theme: {
    extend: {
      fontFamily: {
        sans: ['Inter var', 'Helvetica', 'Arial', 'ui-sans-serif', 'system-ui'],
      },
      gridTemplateColumns: {
        form: 'fit-content(200px) 1fr',
      },
      colors: {
        dlrgRed: {
          DEFAULT: '#E30613',
          50: '#FDA4A9',
          100: '#FC9096',
          200: '#FB6871',
          300: '#FA414C',
          400: '#F91926',
          500: '#E30613',
          600: '#AC050E',
          700: '#76030A',
          800: '#3F0205',
          900: '#080001',
        },
        dlrgYellow: {
          DEFAULT: '#FFED00',
          50: '#FFFAB8',
          100: '#FFF9A3',
          200: '#FFF67A',
          300: '#FFF352',
          400: '#FFF029',
          500: '#FFED00',
          600: '#C7B900',
          700: '#8F8500',
          800: '#575100',
          900: '#1F1C00',
        },
        dlrgGray: {
          DEFAULT: '#575756',
          50: '#B3B3B2',
          100: '#A9A9A8',
          200: '#949493',
          300: '#80807F',
          400: '#6C6C6A',
          500: '#575756',
          600: '#3B3B3A',
          700: '#1F1F1E',
          800: '#020202',
          900: '#000000',
        },
      },
    },
  },
  plugins: [],
};
