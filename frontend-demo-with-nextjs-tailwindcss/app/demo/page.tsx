import {query} from "@/app/demo/query";

export default function Page() {
    return (
        <main>
            <h1>Rest</h1>
            <form>
              <button type="button" onClick={query}>Make API call</button>
            </form>
        </main>
    );
}
