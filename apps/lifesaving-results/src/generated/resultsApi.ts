/**
 * Generated by orval v6.11.1 🍺
 * Do not edit manually.
 * OpenAPI definition
 * OpenAPI spec version: v0
 */
import type {
  MutationFunction,
  QueryFunction,
  QueryKey,
  UseMutationOptions,
  UseQueryOptions,
  UseQueryResult,
} from '@tanstack/react-query';
import { useMutation, useQuery } from '@tanstack/react-query';
import { customInstance } from '../auth/apiClient';
export interface Round {
  round: string;
  isFinal: boolean;
}

export type EventDtoInputValueType =
  (typeof EventDtoInputValueType)[keyof typeof EventDtoInputValueType];

// eslint-disable-next-line @typescript-eslint/no-redeclare
export const EventDtoInputValueType = {
  Time: 'Time',
  Rank: 'Rank',
} as const;

export type EventDtoGender = (typeof EventDtoGender)[keyof typeof EventDtoGender];

// eslint-disable-next-line @typescript-eslint/no-redeclare
export const EventDtoGender = {
  Female: 'Female',
  Male: 'Male',
  Mixed: 'Mixed',
  Unknown: 'Unknown',
} as const;

export type EventDtoEventType =
  (typeof EventDtoEventType)[keyof typeof EventDtoEventType];

// eslint-disable-next-line @typescript-eslint/no-redeclare
export const EventDtoEventType = {
  Individual: 'Individual',
  Team: 'Team',
} as const;

export interface EventDto {
  id?: string;
  version?: number;
  agegroup?: string;
  eventType?: EventDtoEventType;
  gender?: EventDtoGender;
  discipline?: string;
  round?: Round;
  inputValueType?: EventDtoInputValueType;
}

export type TimeDtoGender = (typeof TimeDtoGender)[keyof typeof TimeDtoGender];

// eslint-disable-next-line @typescript-eslint/no-redeclare
export const TimeDtoGender = {
  Female: 'Female',
  Male: 'Male',
  Mixed: 'Mixed',
  Unknown: 'Unknown',
} as const;

export type TimeDtoEventType = (typeof TimeDtoEventType)[keyof typeof TimeDtoEventType];

// eslint-disable-next-line @typescript-eslint/no-redeclare
export const TimeDtoEventType = {
  Individual: 'Individual',
  Team: 'Team',
} as const;

export interface TimeDto {
  id?: string;
  version?: number;
  eventType?: TimeDtoEventType;
  name?: string;
  club?: string;
  nationality?: string;
  agegroup?: string;
  gender?: TimeDtoGender;
  discipline?: string;
  value?: number;
  penalties?: string;
}

export interface CompetitionDto {
  id?: string;
  version?: number;
  uploadId?: string;
  name?: string;
  acronym?: string;
  from?: string;
  till?: string;
}

export interface CompetitionCreated {
  id?: string;
}

export interface CreateCompetition {
  name?: string;
  acronym?: string;
  from?: string;
  till?: string;
}

export interface Swimmer {
  startnumber?: string;
  firstName?: string;
  lastName?: string;
  organization?: string;
  sex?: string;
  yearOfBirth?: number;
}

export interface Start {
  heat?: string;
  lane?: string;
}

export type PenaltyType = (typeof PenaltyType)[keyof typeof PenaltyType];

// eslint-disable-next-line @typescript-eslint/no-redeclare
export const PenaltyType = {
  DidNotStart: 'DidNotStart',
  DidNotFinish: 'DidNotFinish',
  Disqualified: 'Disqualified',
  Points: 'Points',
  None: 'None',
} as const;

export interface Penalty {
  name?: string;
  type?: PenaltyType;
  points?: number;
  realPenalty?: boolean;
}

export interface EntryDto {
  id?: string;
  version?: number;
  number?: string;
  name?: string;
  club?: string;
  nationality?: string;
  timeInMillis?: number;
  placeInHeat?: number;
  penalties?: Penalty[];
  swimmer?: Swimmer[];
  start?: Start;
}

export type EventValueType = (typeof EventValueType)[keyof typeof EventValueType];

// eslint-disable-next-line @typescript-eslint/no-redeclare
export const EventValueType = {
  TimeInMillis: 'TimeInMillis',
  Rank: 'Rank',
} as const;

export type EventCompetitorType =
  (typeof EventCompetitorType)[keyof typeof EventCompetitorType];

// eslint-disable-next-line @typescript-eslint/no-redeclare
export const EventCompetitorType = {
  Individual: 'Individual',
  Team: 'Team',
} as const;

export interface Entry {
  startnumber?: string;
  name?: string;
  organization?: string;
  value?: number;
  penalties?: Penalty[];
  swimmer?: Swimmer[];
  start?: Start;
}

export interface Event {
  agegroup?: string;
  competitorType?: EventCompetitorType;
  sex?: string;
  discipline?: string;
  round?: string;
  isFinal?: boolean;
  valueType?: EventValueType;
  times?: Entry[];
  final?: boolean;
}

export interface Competition {
  name?: string;
  events?: Event[];
}

// eslint-disable-next-line
type SecondParameter<T extends (...args: any) => any> = T extends (
  config: any,
  args: infer P
) => any
  ? P
  : never;

export const importFromJAuswertung = (
  id: string,
  competition: Competition,
  options?: SecondParameter<typeof customInstance>
) => {
  return customInstance<void>(
    {
      url: `/api/import/jauswertung/${id}`,
      method: 'post',
      headers: { 'Content-Type': 'application/json' },
      data: competition,
    },
    options
  );
};

export type ImportFromJAuswertungMutationResult = NonNullable<
  Awaited<ReturnType<typeof importFromJAuswertung>>
>;
export type ImportFromJAuswertungMutationBody = Competition;
export type ImportFromJAuswertungMutationError = unknown;

export const useImportFromJAuswertung = <TError = unknown, TContext = unknown>(options?: {
  mutation?: UseMutationOptions<
    Awaited<ReturnType<typeof importFromJAuswertung>>,
    TError,
    { id: string; data: Competition },
    TContext
  >;
  request?: SecondParameter<typeof customInstance>;
}) => {
  const { mutation: mutationOptions, request: requestOptions } = options ?? {};

  const mutationFn: MutationFunction<
    Awaited<ReturnType<typeof importFromJAuswertung>>,
    { id: string; data: Competition }
  > = (props) => {
    const { id, data } = props ?? {};

    return importFromJAuswertung(id, data, requestOptions);
  };

  return useMutation<
    Awaited<ReturnType<typeof importFromJAuswertung>>,
    TError,
    { id: string; data: Competition },
    TContext
  >(mutationFn, mutationOptions);
};

export const findAll1 = (
  options?: SecondParameter<typeof customInstance>,
  signal?: AbortSignal
) => {
  return customInstance<CompetitionDto[]>(
    { url: `/api/competition`, method: 'get', signal },
    options
  );
};

export const getFindAll1QueryKey = () => [`/api/competition`];

export type FindAll1QueryResult = NonNullable<Awaited<ReturnType<typeof findAll1>>>;
export type FindAll1QueryError = unknown;

export const useFindAll1 = <
  TData = Awaited<ReturnType<typeof findAll1>>,
  TError = unknown
>(options?: {
  query?: UseQueryOptions<Awaited<ReturnType<typeof findAll1>>, TError, TData>;
  request?: SecondParameter<typeof customInstance>;
}): UseQueryResult<TData, TError> & { queryKey: QueryKey } => {
  const { query: queryOptions, request: requestOptions } = options ?? {};

  const queryKey = queryOptions?.queryKey ?? getFindAll1QueryKey();

  const queryFn: QueryFunction<Awaited<ReturnType<typeof findAll1>>> = ({ signal }) =>
    findAll1(requestOptions, signal);

  const query = useQuery<Awaited<ReturnType<typeof findAll1>>, TError, TData>(
    queryKey,
    queryFn,
    queryOptions
  ) as UseQueryResult<TData, TError> & { queryKey: QueryKey };

  query.queryKey = queryKey;

  return query;
};

export const create = (
  createCompetition: CreateCompetition,
  options?: SecondParameter<typeof customInstance>
) => {
  return customInstance<CompetitionCreated>(
    {
      url: `/api/competition`,
      method: 'post',
      headers: { 'Content-Type': 'application/json' },
      data: createCompetition,
    },
    options
  );
};

export type CreateMutationResult = NonNullable<Awaited<ReturnType<typeof create>>>;
export type CreateMutationBody = CreateCompetition;
export type CreateMutationError = unknown;

export const useCreate = <TError = unknown, TContext = unknown>(options?: {
  mutation?: UseMutationOptions<
    Awaited<ReturnType<typeof create>>,
    TError,
    { data: CreateCompetition },
    TContext
  >;
  request?: SecondParameter<typeof customInstance>;
}) => {
  const { mutation: mutationOptions, request: requestOptions } = options ?? {};

  const mutationFn: MutationFunction<
    Awaited<ReturnType<typeof create>>,
    { data: CreateCompetition }
  > = (props) => {
    const { data } = props ?? {};

    return create(data, requestOptions);
  };

  return useMutation<
    Awaited<ReturnType<typeof create>>,
    TError,
    { data: CreateCompetition },
    TContext
  >(mutationFn, mutationOptions);
};

export const findAll = (
  eventId: string,
  options?: SecondParameter<typeof customInstance>,
  signal?: AbortSignal
) => {
  return customInstance<EntryDto[]>(
    { url: `/api/event/${eventId}/entry`, method: 'get', signal },
    options
  );
};

export const getFindAllQueryKey = (eventId: string) => [`/api/event/${eventId}/entry`];

export type FindAllQueryResult = NonNullable<Awaited<ReturnType<typeof findAll>>>;
export type FindAllQueryError = unknown;

export const useFindAll = <TData = Awaited<ReturnType<typeof findAll>>, TError = unknown>(
  eventId: string,
  options?: {
    query?: UseQueryOptions<Awaited<ReturnType<typeof findAll>>, TError, TData>;
    request?: SecondParameter<typeof customInstance>;
  }
): UseQueryResult<TData, TError> & { queryKey: QueryKey } => {
  const { query: queryOptions, request: requestOptions } = options ?? {};

  const queryKey = queryOptions?.queryKey ?? getFindAllQueryKey(eventId);

  const queryFn: QueryFunction<Awaited<ReturnType<typeof findAll>>> = ({ signal }) =>
    findAll(eventId, requestOptions, signal);

  const query = useQuery<Awaited<ReturnType<typeof findAll>>, TError, TData>(
    queryKey,
    queryFn,
    { enabled: !!eventId, ...queryOptions }
  ) as UseQueryResult<TData, TError> & { queryKey: QueryKey };

  query.queryKey = queryKey;

  return query;
};

export const get = (
  id: string,
  options?: SecondParameter<typeof customInstance>,
  signal?: AbortSignal
) => {
  return customInstance<CompetitionDto>(
    { url: `/api/competition/${id}`, method: 'get', signal },
    options
  );
};

export const getGetQueryKey = (id: string) => [`/api/competition/${id}`];

export type GetQueryResult = NonNullable<Awaited<ReturnType<typeof get>>>;
export type GetQueryError = unknown;

export const useGet = <TData = Awaited<ReturnType<typeof get>>, TError = unknown>(
  id: string,
  options?: {
    query?: UseQueryOptions<Awaited<ReturnType<typeof get>>, TError, TData>;
    request?: SecondParameter<typeof customInstance>;
  }
): UseQueryResult<TData, TError> & { queryKey: QueryKey } => {
  const { query: queryOptions, request: requestOptions } = options ?? {};

  const queryKey = queryOptions?.queryKey ?? getGetQueryKey(id);

  const queryFn: QueryFunction<Awaited<ReturnType<typeof get>>> = ({ signal }) =>
    get(id, requestOptions, signal);

  const query = useQuery<Awaited<ReturnType<typeof get>>, TError, TData>(
    queryKey,
    queryFn,
    { enabled: !!id, ...queryOptions }
  ) as UseQueryResult<TData, TError> & { queryKey: QueryKey };

  query.queryKey = queryKey;

  return query;
};

export const findAll2 = (
  id: string,
  options?: SecondParameter<typeof customInstance>,
  signal?: AbortSignal
) => {
  return customInstance<TimeDto[]>(
    { url: `/api/competition/${id}/time`, method: 'get', signal },
    options
  );
};

export const getFindAll2QueryKey = (id: string) => [`/api/competition/${id}/time`];

export type FindAll2QueryResult = NonNullable<Awaited<ReturnType<typeof findAll2>>>;
export type FindAll2QueryError = unknown;

export const useFindAll2 = <
  TData = Awaited<ReturnType<typeof findAll2>>,
  TError = unknown
>(
  id: string,
  options?: {
    query?: UseQueryOptions<Awaited<ReturnType<typeof findAll2>>, TError, TData>;
    request?: SecondParameter<typeof customInstance>;
  }
): UseQueryResult<TData, TError> & { queryKey: QueryKey } => {
  const { query: queryOptions, request: requestOptions } = options ?? {};

  const queryKey = queryOptions?.queryKey ?? getFindAll2QueryKey(id);

  const queryFn: QueryFunction<Awaited<ReturnType<typeof findAll2>>> = ({ signal }) =>
    findAll2(id, requestOptions, signal);

  const query = useQuery<Awaited<ReturnType<typeof findAll2>>, TError, TData>(
    queryKey,
    queryFn,
    { enabled: !!id, ...queryOptions }
  ) as UseQueryResult<TData, TError> & { queryKey: QueryKey };

  query.queryKey = queryKey;

  return query;
};

export const findAll3 = (
  competitionId: string,
  options?: SecondParameter<typeof customInstance>,
  signal?: AbortSignal
) => {
  return customInstance<EventDto[]>(
    { url: `/api/competition/${competitionId}/event`, method: 'get', signal },
    options
  );
};

export const getFindAll3QueryKey = (competitionId: string) => [
  `/api/competition/${competitionId}/event`,
];

export type FindAll3QueryResult = NonNullable<Awaited<ReturnType<typeof findAll3>>>;
export type FindAll3QueryError = unknown;

export const useFindAll3 = <
  TData = Awaited<ReturnType<typeof findAll3>>,
  TError = unknown
>(
  competitionId: string,
  options?: {
    query?: UseQueryOptions<Awaited<ReturnType<typeof findAll3>>, TError, TData>;
    request?: SecondParameter<typeof customInstance>;
  }
): UseQueryResult<TData, TError> & { queryKey: QueryKey } => {
  const { query: queryOptions, request: requestOptions } = options ?? {};

  const queryKey = queryOptions?.queryKey ?? getFindAll3QueryKey(competitionId);

  const queryFn: QueryFunction<Awaited<ReturnType<typeof findAll3>>> = ({ signal }) =>
    findAll3(competitionId, requestOptions, signal);

  const query = useQuery<Awaited<ReturnType<typeof findAll3>>, TError, TData>(
    queryKey,
    queryFn,
    { enabled: !!competitionId, ...queryOptions }
  ) as UseQueryResult<TData, TError> & { queryKey: QueryKey };

  query.queryKey = queryKey;

  return query;
};
