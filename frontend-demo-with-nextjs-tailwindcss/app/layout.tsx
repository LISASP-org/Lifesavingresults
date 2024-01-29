import type {Metadata} from "next";

import "./globals.css";

export const metadata: Metadata = {
    title: "DLRG Rettungssport",
    description: "Rettungssport in der DLRG",
};

export default function RootLayout({ children }: Readonly<{ children: React.ReactNode; }>) {
    return (
        <html lang="de">
        <head>
            <link rel="icon" href="/favicon.ico" sizes="any"/>
        </head>
        <body className="flex flex-col">
        <header className="p-3">
            <nav className="relative flex w-full items-center justify-between md:flex-wrap md:justify-start">
                <ul className="mr-auto flex flex-col lg:flex-row">
                    <li className="block pb-2 pt-2 pr-6">
                        <a href="/">Rettungssport</a>
                    </li>
                    <li className="block pb-2 pt-2 pr-6">
                        <a href="/competitions">Wettkämpfe</a>
                    </li>
                    <li className="block pb-2 pt-2 pr-6">
                        <a href="/registration">Meldung</a>
                    </li>
                    <li className="block pb-2 pt-2 pr-6">
                        <a href="/results">Ergebnisse</a>
                    </li>
                    <li className="block pb-2 pt-2 pr-6">
                        <a href="/times">Zeiten</a>
                    </li>
                    <li className="block pb-2 pt-2 pr-6">
                        <a href="/demo">Demo</a>
                    </li>
                    <li className="block pb-2 pt-2 pr-6">
                        <a href="/about">Über</a>
                    </li>
                </ul>
            </nav>
        </header>
        <div className="flex justify-center">
            <div>{children}</div>
        </div>
        <footer className="p-4">
            <nav className="relative flex w-full items-center justify-between md:flex-wrap md:justify-start">
                <ul className="mr-auto flex flex-col lg:flex-row">
                    <li className="block pb-2 pt-2 pr-6">
                        <a href="#!">Impressum</a>
                    </li>
                    <li className="block pb-2 pt-2 pr-6">
                        <a href="#!">Datenschutz</a>
                    </li>
                </ul>
            </nav>
        </footer>
        </body>
        </html>
    );
}
