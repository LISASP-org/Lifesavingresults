'use server'

export async function callAPI() {
    try {
        const res = await fetch(
            "https://jsonplaceholder.typicode.com/posts/1"
        );
        const data = await res.json();
        console.log(data);
    } catch (err) {
        console.log(err);
    }
}

export async function query() {
    startTransition(() => {
        callAPI();
    });
}