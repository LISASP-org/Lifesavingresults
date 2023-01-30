export default {
  resultsApi: {
    input: 'http://localhost:9001/api-docs',
    output: {
      target: './src/generated/resultsApi.ts',
      client: 'react-query',
      override: {
        mutator: {
          path: './src/auth/apiClient.ts',
          name: 'customInstance',
        },
      },
    },
  },
};
