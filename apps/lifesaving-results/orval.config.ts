export default {
  resultsApi: {
    input: 'http://localhost:9001/api-docs',
    output: {
      target: './src/generated/resultsApi.ts',
      tsconfig: './tsconfig.json',
      client: 'react-query',
      prettier: true,
      override: {
        mutator: {
          path: './src/auth/apiClient.ts',
          name: 'customInstance',
        },
      },
    },
  },
};
