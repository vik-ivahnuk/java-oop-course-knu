import React, {useEffect, useState} from "react";
import {useKeycloak} from "@react-keycloak/web";
import {useNavigate} from "react-router-dom";

type CurrentAdmin = {
    id: number;
    username: string;
};

type Tours = {
    id: number;
    name: string;
    price: string;
    count: number;
};

export const AdminPage = () => {
    const {keycloak} = useKeycloak();
    const navigate = useNavigate();
    const [currentAdmin, setCurrentAdmin] = useState<CurrentAdmin>();
    const [tours, setTours] = useState<Tours[]>();

    const updateCurrentAdmin = () => {
        fetch("http://localhost:8080/servlet_war_exploded/current-admin", {
            method: "GET",
            headers: {Authorization: "Bearer " + keycloak.token},
        })
            .then((r) => r.json())
            .then(client => {
                setCurrentAdmin(client);
            })
            .catch(console.error);
    }

    const updateTours = () => {
        fetch("http://localhost:8080/servlet_war_exploded/tour")
            .then((r) => r.json())
            .then(setTours)
            .catch(console.error);
    }

    const addTour = (id: number) => {
        fetch(
            "http://localhost:8080/servlet_war_exploded/refresh",
            {
                method: "PUT",
                headers: {
                    Authorization: "Bearer " + keycloak.token,
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({id: id}),
            }
        )
            .then(() => updateScreen())
            .catch(console.error);
    }


    const updateScreen = () => {
        updateCurrentAdmin()
        updateTours()
    };

    useEffect(() => {
        setTimeout(() => {
            if (!keycloak.authenticated) {
                navigate("/");
                return;
            }
            updateScreen();
        }, 300);
    }, []);

    if (!currentAdmin || !tours) {
        return <h1>Loading...</h1>;
    }

    return (
        <>
            <h1>
                {currentAdmin.username} (id = {currentAdmin.id})
            </h1>
            <button onClick={() => keycloak.logout()}>Logout</button>
            <table id="goods">
                <thead>
                <tr>
                    <th>Name</th>
                    <th>Price</th>
                    <th>Count exist</th>
                    <th>Refresh</th>
                </tr>
                </thead>
                <tbody>
                {tours.map((g) => (
                    <tr key={"tr_" + g.id}>
                        <td>{g.name}</td>
                        <td>{g.price}</td>
                        <td>{g.count}</td>
                        <td>
                            <button onClick={() => addTour(g.id)}>
                                Add
                            </button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </>
    );
};
